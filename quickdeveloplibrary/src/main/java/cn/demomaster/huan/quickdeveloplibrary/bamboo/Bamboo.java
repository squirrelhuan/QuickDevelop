package cn.demomaster.huan.quickdeveloplibrary.bamboo;

/**
 * 竹节 流程控制 squirrel桓
 */
public class Bamboo {
    Node rootNode;
    Node currentNode;
    public Bamboo add(Node node){
        if(rootNode==null){
            rootNode = node;
            currentNode = node;
        }else {
            currentNode.setNextNode(node);
            currentNode = node;
        }
        return this;
    }

    public void start() {
        if(rootNode!=null){
            rootNode.doJob(rootNode);
        }
    }

    public static abstract class Node implements OnCompleteListener{
        private Node nextNode;

        public void setNextNode(Node child) {
            this.nextNode = child;
        }

        public Node getNextNode() {
            return nextNode;
        }

        /**
         * 工作结束提交结果
         * @param result
         */
        public void submit(Object... result){
            if(nextNode!=null){
                nextNode.doJob(nextNode,result);
            }
        }
    }

    public static interface OnCompleteListener{
        void doJob(Node node,Object... result);
    }
}
