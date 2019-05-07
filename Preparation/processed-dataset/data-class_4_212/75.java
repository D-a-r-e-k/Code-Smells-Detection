@Override
public void moveTargets(TargetGroup sourceGroup, TargetGroup targetGroup, List<Long> targetsToMove) {
    Iterator<Long> it = targetsToMove.iterator();
    while (it.hasNext()) {
        Long targetToMove = it.next();
        //add the target to the new group  
        GroupMemberDTO dto = createGroupMemberDTO(targetGroup, targetToMove);
        targetGroup.getNewChildren().add(dto);
        //remove the target from the old group  
        sourceGroup.getRemovedChildren().add(targetToMove);
    }
    save(sourceGroup);
    save(targetGroup);
}
