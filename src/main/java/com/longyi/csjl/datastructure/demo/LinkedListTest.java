package com.longyi.csjl.datastructure.demo;

/**
 * @author ly
 * @description TODO
 * @date 2021/1/24 16:04
 * @throw
 */
public class LinkedListTest {


    /**
     * 删除一个链表中所有为x的元素 不使用虚拟头结点方案
     * @param head
     * @param val
     * @return
     */
    public ListNodeNew removeElements(ListNodeNew head, int val){
        while (head!=null && head.val == val){
            head=head.next;
        }
        if(head==null){
            return null;
        }
        ListNodeNew pre=head;
        while (pre.next!=null){
            if(pre.next.val==val){
                pre.next=pre.next.next;
            }else{
                pre=pre.next;
            }
        }
        return head;
    }



    public ListNodeNew removeElementsVirtual(ListNodeNew head, int val){
        ListNodeNew virtualNode=new ListNodeNew(-1);
        virtualNode.next=head;
        ListNodeNew pre=virtualNode;
        while (pre.next!=null){
            if(pre.next.val==val){
                pre.next=pre.next.next;
            }else{
                pre=pre.next;
            }
        }
        return head;
    }


}    
   