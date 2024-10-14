1. I coded stage() and it works great as far as I can tell
2. I coded commit() and everything works great, except I do not think I am updating the head properly. The third time I make the commit, the line that says "parent: " is the same as that line but on the second commit.
3. I did not do checkout()
4. - Originally my updateHead method was just putting the contents of the commitFile straight into the head file, not the sha of the commitFile

- also did not initialize the Linked List
- also at first, any commit I would try to make after the first one would result in an error, but I fixed that

Note: sometimes I make a blob of a file in order to be able to call the method that gets the sha of that file
