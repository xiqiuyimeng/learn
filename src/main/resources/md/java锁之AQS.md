- aqs （AbstractQueuedSynchronizer）

  ~~~java
  // 采用双向链表(FIFO)来管理线程
  // 单个的链表节点 Node 类，主要的成员变量
  // 链表示意图
             +------+  prev +-----+       +-----+
        head |      | <---- |     | <---- |     |  tail
             +------+       +-----+       +-----+
  
  static final class Node {
      /** Marker to indicate a node is waiting in shared mode */
      static final Node SHARED = new Node();
      /** Marker to indicate a node is waiting in exclusive mode */
      static final Node EXCLUSIVE = null;
  
      /** waitStatus value to indicate thread has cancelled */
      static final int CANCELLED =  1;
      /** waitStatus value to indicate successor's thread needs unparking */
      static final int SIGNAL    = -1;
      /** waitStatus value to indicate thread is waiting on condition */
      static final int CONDITION = -2;
      /**
       * waitStatus value to indicate the next acquireShared should
       * unconditionally propagate
       */
      static final int PROPAGATE = -3;
  
      volatile Node prev;
  
      volatile Node next;
  
      volatile Thread thread;
      
  	Node nextWaiter;
  }
  
  
  ~~~

  

