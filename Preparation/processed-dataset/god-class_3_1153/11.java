@ManagedAttribute
public double getAverageBlockingTime() {
    return num_blockings == 0 ? 0 : total_block_time / num_blockings;
}
