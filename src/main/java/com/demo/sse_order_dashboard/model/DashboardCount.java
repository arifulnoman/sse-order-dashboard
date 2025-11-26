package com.demo.sse_order_dashboard.model;

public class DashboardCount {

	private long pending;
	private long approved;
	private long delivered;
	private long total;

	public DashboardCount(long pending, long approved, long delivered, long total) {
		this.pending = pending;
		this.approved = approved;
		this.delivered = delivered;
		this.total = total;
	}

	public long getPending() {
		return pending;
	}

	public long getApproved() {
		return approved;
	}

	public long getDelivered() {
		return delivered;
	}

	public long getTotal() {
		return total;
	}
}
