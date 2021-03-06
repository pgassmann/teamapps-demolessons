package org.teamapps.demolessons.p5_hibernate_sql;

import org.hibernate.Session;
import org.teamapps.data.value.SortDirection;
import org.teamapps.data.value.Sorting;
import org.teamapps.demolessons.p5_hibernate_sql.models.Invoice;
import org.teamapps.ux.component.table.AbstractTableModel;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class InvoiceTableModel extends AbstractTableModel<Invoice> {
	private final Session session;
	private final Map<String, String> textFilterByFieldName = new HashMap<>();
	private LocalDateTime filterCreatedFrom;
	private LocalDateTime filterCreatedUntil;

	public InvoiceTableModel(Session session) {
		this.session = session;
	}

	@Override
	public int getCount() {
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root root = cq.from(Invoice.class);
		cq.select(cb.count(root));
		cq.where(createFiltersPredicate(cb, root));
		return session.createQuery(cq).getSingleResult().intValue();
	}

	@Override
	public List<Invoice> getRecords(int startIndex, int length, Sorting sorting) {
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Invoice> cq = cb.createQuery(Invoice.class);
		Root root = cq.from(Invoice.class);
		cq.where(createFiltersPredicate(cb, root));
		if (sorting.getFieldName() != null) {
			Path orderPath = root.get(sorting.getFieldName());
			if (sorting.getSorting() == SortDirection.ASC) {
				cq.orderBy(cb.asc(orderPath));
			} else {
				cq.orderBy(cb.desc(orderPath));
			}
		}
		return session.createQuery(cq)
				.setFirstResult(startIndex)
				.setMaxResults(length)
				.getResultList();
	}

	private <T> Predicate createFiltersPredicate(CriteriaBuilder cb, Root root) {
		List<Predicate> predicates = new ArrayList<>();
		textFilterByFieldName.forEach((fieldName, searchString) -> {
			predicates.add(cb.like(root.get(fieldName), searchString));
		});
		if (filterCreatedFrom != null) {
			predicates.add(cb.greaterThanOrEqualTo(root.get(Invoice.FIELD_CREATED), filterCreatedFrom));
		}
		if (filterCreatedUntil != null) {
			predicates.add(cb.lessThanOrEqualTo(root.get(Invoice.FIELD_CREATED), filterCreatedUntil));
		}
		return cb.and(predicates.toArray(new Predicate[predicates.size()]));
	}

	// Called by the Filter Fields in the HeaderRow of a column.
	public void setTextFilter(String fieldName, String searchString) {
		if (isNotBlank(searchString)) {
			textFilterByFieldName.put(fieldName, searchString);
		} else {
			textFilterByFieldName.remove(fieldName);
		}
		onAllDataChanged.fire();
	}

	public LocalDateTime getFilterCreatedFrom() {
		return filterCreatedFrom;
	}

	public void setFilterCreatedFrom(LocalDateTime filterCreatedFrom) {
		this.filterCreatedFrom = filterCreatedFrom;
		onAllDataChanged.fire();
	}

	public LocalDateTime getFilterCreatedUntil() {
		return filterCreatedUntil;
	}

	public void setFilterCreatedUntil(LocalDateTime filterCreatedUntil) {
		this.filterCreatedUntil = filterCreatedUntil;
		onAllDataChanged.fire();
	}
}
