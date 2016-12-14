#
def ways(di, offset, steps):
    global mem, dimensions
    if steps in mem[di] and offset in mem[di][steps]:
        return mem[di][steps][offset]
    val = 0
    if steps == 0:
        val = 1
    else:
        if offset - 1 >= 1:
            val += ways(di, offset - 1, steps - 1)
        if offset + 1 <= dimensions[di]:
            val += ways(di, offset + 1, steps - 1)
    mem[di][steps][offset] = val
    return val


def set_ways(left, right, steps):
    # must create t1, t2, t3 .. ti for steps
    global mem_set, mem, starting_point
    #print left, right
    #sleep(2)
    if (left, right) in mem_set and steps in mem_set[(left, right)]:
        return mem_set[(left, right)][steps]
    if right - left == 1:
        #print 'getting steps for', left, steps, starting_point[left]
        #print 'got ', mem[left][steps][starting_point[left]], 'steps'
        return mem[left][steps][starting_point[left]]
        #return ways(left, starting_point[left], steps)
    val = 0
    split_point =  left + (right - left) / 2 
    for i in xrange(steps + 1):
        t1 = i
        t2 = steps - i
        mix_factor = fact[steps] / (fact[t1] * fact[t2])
        #print "mix_factor = %d, dimension: %d - %d steps, dimension %d - %d steps" % (mix_factor, left, t1, split_point, t2)
        val += mix_factor * set_ways(left, split_point, t1) * set_ways(split_point, right, t2)
    mem_set[(left, right)][steps] = val
    return val

import sys
from time import sleep, time

fact = {}
fact[0] = 1
start = time()
accum = 1
for k in xrange(1, 300+1):
    accum *= k
    fact[k] = accum
#print 'fact_time', time() - start

data = sys.stdin.readlines()
num_tests = int(data.pop(0))
for ignore in xrange(0, num_tests):
    n_and_steps = data.pop(0)
    n, steps = map(lambda x: int(x), n_and_steps.split())
    starting_point = map(lambda x: int(x), data.pop(0).split())
    dimensions = map(lambda x: int(x), data.pop(0).split())
    mem = {}
    for di in xrange(n):
        mem[di] = {}
        for i in xrange(steps + 1):
            mem[di][i] = {}
            ways(di, starting_point[di], i)
    start = time()
    #print 'mem vector is done'
    mem_set = {}
    for i in xrange(n + 1):
        for j in xrange(n + 1):
            mem_set[(i, j)] = {}
    answer = set_ways(0, n, steps)
    #print answer
    print answer % 1000000007
    #print time() - start
