public void resetStats() {
    super.resetStats();
    num_blockings = total_block_time = num_replenishments_received = num_credit_requests_sent = num_bytes_sent = 0;
    num_replenishments_sent = num_credit_requests_received = 0;
    blockings.clear();
}
