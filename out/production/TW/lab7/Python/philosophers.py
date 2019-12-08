import threading
import random
import time
import datetime


class Philosopher(threading.Thread):

    def __init__(self, num, leftFork, rightFork):
        threading.Thread.__init__(self)
        self.num = num
        self.leftFork = leftFork if num%2 == 0 else rightFork
        self.rightFork = rightFork if num%2 == 0 else leftFork
        self.times = []
 
    def sleep(self):
        # print("Sleeping", self.num)
        time.sleep( random.uniform(1,10)/1000)

    def run(self):
        while(self.running):
            self.sleep()
            self.eat()
        print(sum(self.times)/len(self.times))
 
    def eat(self):
        forkL, forkR = self.leftFork, self.rightFork
        start = time.clock()
        forkL.acquire()
        forkR.acquire()
        end = time.clock()
        self.times.append(end-start)
        # print("Eating", self.num)
        time.sleep(random.uniform(1,10)/1000)
        forkR.release()
        forkL.release()		
        
 
def Main():

    forks = [threading.Lock() for n in range(5)]
    philosophers= [Philosopher(i, forks[i], forks[(i+1)%5]) for i in range(5)]

    Philosopher.running = True
    for p in philosophers: p.start()
    time.sleep(10)
    Philosopher.running = False
 
Main()