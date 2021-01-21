package com.lt.recyclerdemo

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    class ListNode(val value:Int){
        var next:ListNode? = null
    }

    @Test
    fun linkReverse(){
        val head = generateLink()
        printList(head)
        val r = reverseMN(head,4,8)
        println()
        printList(r)
        println()
    }
    var nPlus:ListNode? = null
    private fun reverseN(head: ListNode, n: Int):ListNode {
        if(head.next == null || n == 1) {
            nPlus = head.next
            return head
        }
        val last = reverseN(head.next!!, n-1)
        head.next!!.next = head
        head.next = nPlus
        return last
    }
    var mPlus:ListNode? = null
    private fun reverseMN(head: ListNode, m: Int, n: Int): ListNode {
        if (m == 1) {
            return reverseN(head, n)
        }
        head.next = reverseMN(head.next!!,m-1,n-1)
        return head
    }

    private fun reverse(head:ListNode):ListNode{
        if(head.next == null) return head
        val last = reverse(head.next!!)
        head.next!!.next = head
        head.next = null
        return last

    }

    private fun generateLink():ListNode{
        val head = ListNode(0)
        var node:ListNode? = head
        for (i in 1 until 10) {
            node?.next = ListNode(i)
            node = node?.next
        }
        return head
    }

    private fun printList(head: ListNode) {
        print("${head.value}")
        if (head.next == null) {
            return
        }
        print("->")
        printList(head.next!!)
    }
}