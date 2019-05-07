public void drop(DropTargetDropEvent dtde) {
    try {
        int dropAction = dtde.getDropAction();
        Transferable t = dtde.getTransferable();
        final MainView mainView = (MainView) dtde.getDropTargetContext().getComponent();
        NodeView targetNodeView = mainView.getNodeView();
        MindMapNode targetNode = targetNodeView.getModel();
        MindMapNodeModel targetNodeModel = (MindMapNodeModel) targetNode;
        // Intra application DnD 
        // For some reason, getting sourceAction is only possible for local 
        // transfer. When I try to remove clause dtde.isLocalTransfer, I get 
        // an answer 
        // like "no drop current". One hypothesis is that with nonlocal 
        // transfers, I 
        // have to accept drop action before I can get transfer data. 
        // However, this is 
        // not what I want in this particular situation. A part of the 
        // problem lies in 
        // the hackery of sending source action using data flavour too. 
        if (dtde.isLocalTransfer() && t.isDataFlavorSupported(MindMapNodesSelection.dropActionFlavor)) {
            String sourceAction = (String) t.getTransferData(MindMapNodesSelection.dropActionFlavor);
            if (sourceAction.equals("LINK")) {
                dropAction = DnDConstants.ACTION_LINK;
            }
            if (sourceAction.equals("COPY")) {
                dropAction = DnDConstants.ACTION_COPY;
            }
        }
        mainView.setDraggedOver(NodeView.DRAGGED_OVER_NO);
        mainView.repaint();
        if (dtde.isLocalTransfer() && (dropAction == DnDConstants.ACTION_MOVE) && !isDropAcceptable(dtde)) {
            dtde.rejectDrop();
            return;
        }
        dtde.acceptDrop(dtde.getDropAction());
        if (!dtde.isLocalTransfer()) {
            // if 
            // (dtde.isDataFlavorSupported(MindMapNodesSelection.fileListFlavor)) 
            // { 
            // System.err.println("filelist"); 
            mMindMapController.paste(t, targetNode, mainView.dropAsSibling(dtde.getLocation().getX()), mainView.dropPosition(dtde.getLocation().getX()));
            dtde.dropComplete(true);
            return;
        }
        // This all is specific to MindMap model. Needs rewrite to work for 
        // other modes. 
        // We ignore data transfer in dtde. We take selected nodes as drag 
        // sources. 
        // <problem> 
        // The behaviour is not so fine, when some of selected nodes is an 
        // ancestor of other selected nodes. 
        // Ideally, we would first unselect all nodes, which have an 
        // ancestor among selected nodes. 
        // I don't have time/lust to do this. This is just a minor problem. 
        // </problem> 
        // By transferable object we only transfer source action. This will 
        // be a problem, when we want 
        // to implement extra application dnd or dnd between different Java 
        // Virtual Machines. 
        if (dropAction == DnDConstants.ACTION_LINK) {
            // ACTION_LINK means for us change the color, style and font. 
            // This is not very clean. This all should probably be 
            // implemented on the mindMapMapModel level. On the other 
            // hand, one would have to downcast to MindMapMapModel anyway. 
            // MindMapNode selectedNode = 
            // c.getView().getSelected().getModel(); 
            MindMapMapModel mindMapMapModel = (MindMapMapModel) mMindMapController.getModel();
            // link feature continues here. fc, 01.11.2003: 
            // if there are more than 4 nodes, then ask the user: 
            int yesorno = JOptionPane.YES_OPTION;
            if (mMindMapController.getView().getSelecteds().size() >= 5) {
                yesorno = JOptionPane.showConfirmDialog(mMindMapController.getFrame().getContentPane(), mMindMapController.getText("lots_of_links_warning"), Integer.toString(mMindMapController.getView().getSelecteds().size()) + " links to the same node", JOptionPane.YES_NO_OPTION);
            }
            if (yesorno == JOptionPane.YES_OPTION) {
                for (ListIterator it = mMindMapController.getView().getSelecteds().listIterator(); it.hasNext(); ) {
                    MindMapNodeModel selectedNodeModel = (MindMapNodeModel) ((NodeView) it.next()).getModel();
                    // mindMapMapModel.setNodeColor(selectedNodeModel,targetNode.getColor()); 
                    // mindMapMapModel.setNodeFont(selectedNodeModel,targetNode.getFont()); 
                    mMindMapController.addLink(selectedNodeModel, targetNodeModel);
                }
            }
        } else {
            if (!targetNode.isWriteable()) {
                String message = mMindMapController.getText("node_is_write_protected");
                JOptionPane.showMessageDialog(mMindMapController.getFrame().getContentPane(), message, "Freemind", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Transferable trans = null;
            // if move, verify, that the target is not a son of the sources. 
            List selecteds = mMindMapController.getSelecteds();
            if (DnDConstants.ACTION_MOVE == dropAction) {
                MindMapNode actualNode = targetNode;
                do {
                    if (selecteds.contains(actualNode)) {
                        String message = mMindMapController.getText("cannot_move_to_child");
                        JOptionPane.showMessageDialog(mMindMapController.getFrame().getContentPane(), message, "Freemind", JOptionPane.WARNING_MESSAGE);
                        dtde.dropComplete(true);
                        return;
                    }
                    actualNode = (actualNode.isRoot()) ? null : actualNode.getParentNode();
                } while (actualNode != null);
                trans = mMindMapController.cut();
            } else {
                trans = mMindMapController.copy();
            }
            mMindMapController.getView().selectAsTheOnlyOneSelected(targetNodeView);
            boolean result = mMindMapController.paste(trans, targetNode, mainView.dropAsSibling(dtde.getLocation().getX()), mainView.dropPosition(dtde.getLocation().getX()));
            if (!result && DnDConstants.ACTION_MOVE == dropAction) {
            }
        }
    } catch (Exception e) {
        System.err.println("Drop exception:" + e);
        freemind.main.Resources.getInstance().logException(e);
        dtde.dropComplete(false);
        return;
    }
    dtde.dropComplete(true);
}
