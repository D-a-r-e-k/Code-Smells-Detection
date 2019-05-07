// Carry over generation numbers from another SegmentInfos 
void updateGeneration(SegmentInfos other) {
    lastGeneration = other.lastGeneration;
    generation = other.generation;
}
