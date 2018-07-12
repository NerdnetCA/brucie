package ca.nerdnet.brucie.core.util;

/** This file is a stub. The purpose it to be a template for the
 * production Tree-as-array code.
 */
interface ITree {

    void getRoot(TreeNode node);
    boolean parentOf(int nodeId, TreeNode parentNode);
    void getNode(int nodeId, TreeNode node);
    void getLeftChild(int nodeId, TreeNode childNode);
    void getRightChild(int nodeId, TreeNode childNode);


    public static class TreeNode {
        int nodeId;
        int nodeData;
    }

}
