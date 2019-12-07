import lorem
import random


text = ' '.join([lorem.paragraph() for i in range(20000)])

print("Generated ", len(text.split('.')), "sentences")

print("inserting 100 spaces")

words = text.split(' ')

indeces = random.sample(range(len(words)), 100)

for i in indeces:
    words[i] = words[i] + ' '

text = ' '.join(words)

f = open('text.txt', 'w')
f.write(text)