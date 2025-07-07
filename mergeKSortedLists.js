class ListNode {
  constructor(val, next = null) {
    this.val = val;
    this.next = next;
  }
}

class MinHeap {
  constructor() {
    this.heap = [];
  }

  size() {
    return this.heap.length;
  }

  isEmpty() {
    return this.size() === 0;
  }

  // Вставка нового элемента
  push(node) {
    this.heap.push(node);
    this._heapifyUp();
  }

  // Удаление и возврат минимального элемента
  pop() {
    if (this.isEmpty()) return null;
    const min = this.heap[0];
    const last = this.heap.pop();
    if (!this.isEmpty()) {
      this.heap[0] = last;
      this._heapifyDown();
    }
    return min;
  }

  _heapifyUp() {
    let index = this.size() - 1;
    const node = this.heap[index];
    while (index > 0) {
      const parentIndex = Math.floor((index - 1) / 2);
      const parent = this.heap[parentIndex];
      if (node.val >= parent.val) break;
      this.heap[index] = parent;
      index = parentIndex;
    }
    this.heap[index] = node;
  }

  _heapifyDown() {
    let index = 0;
    const length = this.size();
    const node = this.heap[index];

    while (true) {
      let leftIdx = 2 * index + 1;
      let rightIdx = 2 * index + 2;
      let smallest = index;

      if (leftIdx < length && this.heap[leftIdx].val < this.heap[smallest].val) {
        smallest = leftIdx;
      }
      if (rightIdx < length && this.heap[rightIdx].val < this.heap[smallest].val) {
        smallest = rightIdx;
      }
      if (smallest === index) break;

      this.heap[index] = this.heap[smallest];
      index = smallest;
    }
    this.heap[index] = node;
  }
}

function mergeKLists(lists) {
  const minHeap = new MinHeap();

  for (let node of lists) {
    if (node !== null) minHeap.push(node);
  }

  const dummy = new ListNode(0);
  let tail = dummy;

  while (!minHeap.isEmpty()) {
    const minNode = minHeap.pop();
    tail.next = minNode;
    tail = tail.next;

    if (minNode.next !== null) {
      minHeap.push(minNode.next);
    }
  }

  return dummy.next;
}
