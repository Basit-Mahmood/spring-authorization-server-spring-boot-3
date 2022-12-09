package pk.training.basit.authorizationserver.jpa.audit;

import java.time.Instant;

import org.springframework.data.annotation.AccessType;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
@AccessType(AccessType.Type.FIELD)
public class AuditDeletedDate extends Audit {

	@Column(name = "deleted_date")
	private Instant deletedDate;
	
	public AuditDeletedDate() {
		
	}

	public AuditDeletedDate(Audit audit, Instant deletedDate) {
		super(audit);
		this.deletedDate = deletedDate;
	}

	public AuditDeletedDate(Long createdBy, Instant createdDate, Long lastModifiedBy, Instant lastModifiedDate, Instant deletedDate) {
		super(createdBy, createdDate, lastModifiedBy, lastModifiedDate);
		this.deletedDate = deletedDate;
	}

	public Instant getDeletedDate() {
		return deletedDate;
	}

	public void setDeletedDate(Instant deletedDate) {
		this.deletedDate = deletedDate;
	}
	
}
