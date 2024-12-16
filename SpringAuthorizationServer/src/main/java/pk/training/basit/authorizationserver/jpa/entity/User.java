package pk.training.basit.authorizationserver.jpa.entity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Basic;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import pk.training.basit.authorizationserver.jpa.audit.AuditDeletedDate;

/**
 * The first two and most basic JPA mapping annotations are @javax.persistence.Entity and @javax.persistence.Table. @Entity marks a 
 * class to indicate that it is an entity. Every entity must have this annotation. By default, an entity name is equal to the 
 * unqualified entity class name, so the following pk.training.basit.entities.Book class has an entity name of Author.
 * 
 * The table name to which an entity maps defaults is the entity name. So the default table name for UserPrincipal is UserPrincipal. 
 * uniqueConstraints is a special attribute used exclusively for schema generation. JPA providers can automatically generate 
 * database schema based off of your entities, and this attribute enables you to indicate that a particular column or columns should
 * form a unique constraint. 
 * As of JPA 2.1, you can also use the indexes attribute to specify indexes that JPA should create when using schema generation.
 */
@Entity
@Table(
	name = "UserPrincipal",
	uniqueConstraints = {
        @UniqueConstraint(
    	    name = "UserPrincipal_Username", 
    	    columnNames = "Username"
        )
    }
)
/**
 * When you design your system, ultimately you need to decide whether you want to couple the user object for persistence purposes 
 * with the user object for authentication and authorization purposes. The advantage to separating them is that you really don’t 
 * need (or even want) a mutable user object carried around in the security context, and keeping them separate enforces this mantra. 
 * However, the advantage to making them the same object is that you don't have to do as much work to embed the user entity in other
 * entities. Ultimately, only you can decide the approach that works best for you.
 * 
 * For simplicity, the Customer Support application combines the UserDetails implementation and the entity in the same object. The 
 * changes to UserPrincipal are pretty simple. It now implements UserDetails instead of Authentication and also implements 
 * org.springframework.security.core.CredentialsContainer so that Spring Security clears the password when the authentication process
 * is complete.
 */
public class User implements UserDetails, CredentialsContainer, Cloneable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_generator")
    @SequenceGenerator(
        name = "user_id_generator",
        sequenceName = "user_seq",
        allocationSize = 1
    )
	private Long id;
	
	@Basic
	private String username;
	
	/**
	 * You can use @Basic’s optional attribute to indicate that the property may be null. (This is only a hint and is useful for 
	 * schema generation.) This is ignored for primitives, which may never be null.
	 */
	@Basic(fetch = FetchType.LAZY)
    @Column(name = "HashedPassword")
    private String hashedPassword;
	
	private boolean enabled;
	private boolean accountNonExpired;
	private boolean credentialsNonExpired;
	private boolean accountNonLocked;
	
	// @CollectionTable in UserPrincipal maps UserAuthority to the UserPrincipalAuthority table.
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
    	name = "UserAuthority", 
    	joinColumns = {
            @JoinColumn(
            	name = "UserId", 
            	referencedColumnName = "Id"
            )
       }
    )
    private Set<UserAuthority> authorities = new HashSet<>();
    
    @Embedded
    private AuditDeletedDate audit = new AuditDeletedDate();
   
    public User() {
		
	}

    public User(Long id, String username, String password, Set<UserAuthority> authorities) {
		this(id, username, password, true, true, true, true, authorities, null);
	}
    
	public User(Long id, String username, String password, boolean enabled, boolean accountNonExpired, 
			boolean credentialsNonExpired, boolean accountNonLocked, Set<UserAuthority> authorities, AuditDeletedDate audit) {
		this.id = id;
		this.username = username;
		this.hashedPassword = password;
		this.enabled = enabled;
		this.accountNonExpired = accountNonExpired;
		this.credentialsNonExpired = credentialsNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.authorities = Collections.unmodifiableSet(authorities);
		this.audit = audit;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

    public String getHashedPassword() {
        return this.hashedPassword;
    }

    public void setHashedPassword(String password) {
        this.hashedPassword = password;
    }
    
    @Transient
    @Override
    public String getPassword() {
        return this.getHashedPassword() == null ? null : this.getHashedPassword();
    }

    @Override
    public void eraseCredentials() {
        this.hashedPassword = null;
    }
	
    @Override
    public Set<UserAuthority> getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(Set<UserAuthority> authorities) {
        this.authorities = authorities;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public AuditDeletedDate getAudit() {
		return audit;
	}

	public void setAudit(AuditDeletedDate audit) {
		this.audit = audit;
	}

	@Override
    public int hashCode() {
        return this.username.hashCode();
    }

    @Override
    public boolean equals(Object other) {
    	return other instanceof User && ((User)other).id == this.id;
    }

    @Override
    protected User clone() {
    	try {
            return (User)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e); // not possible
        }
    }

    @Override
    public String toString() {
        return this.username;
    }
    
}
