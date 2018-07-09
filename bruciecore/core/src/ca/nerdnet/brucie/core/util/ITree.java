package ca.nerdnet.brucie.core.util;

/** This file is a stub. The purpose it to be a template for the
 * production Tree-as-array code.
 */
public abstract class ITree {

    private int[] mData;

    public void getRoot(TreeNode node) {
        node.nodeId = 1;
        node.nodeData = mData[1];
    }

    public boolean parentOf(int nodeId, TreeNode parentNode) {
        if(nodeId == 1) return false;

        int i = nodeId / 2;
        parentNode.nodeId = i;
        parentNode.nodeData = mData[i];

        return true;
    }

    public void getNode(int nodeId, TreeNode node) {
        node.nodeData = mData[nodeId];
        node.nodeId = nodeId;
    }

    public void getLeftChild(int nodeId, TreeNode childNode) {
        int i = nodeId*2;
        childNode.nodeId = i;
        childNode.nodeData = mData[i];
    }

    public void getRightChild(int nodeId, TreeNode childNode) {
        int i = nodeId*2 + 1;
        childNode.nodeId = i;
        childNode.nodeData = mData[i];
    }


    public class TreeNode {
        int nodeId;
        int nodeData;
    }

}
