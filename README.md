# APS
An Advanced Planning & Scheduling system based on genetic algorithms.

## Chromosome Encoding
Use a 1-dimensional array to represent an arrangement of tasks. Each 
task includes two elements: one element designates the work group to which
the task is allocated, and the other stores the sequence number in that 
group.

## Crossover
When doing a crossover, we do not allow direct swapping of genes in two 
chromosomes. Instead, swapping can only occur between two workgroups
with similar skills.

## Mutation
There are two types of mutations:
1. Swapping the sequence of two jobs in the same work group
2. Moving one or more tasks from one group to another (1-directional and without swapping)

## Cost function
The cost function can be customized to rule in multiple factors:
1. Number of late days (or $days \times pieces$, or $days \times amount$)
