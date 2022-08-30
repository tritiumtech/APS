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
2. Arbitrary penalties applied to special cases, such as 
    * Repeating orders allocated to an inexperienced work group
    * Brand X is a prioritized client, its penalty of delay is set to five times of regular cost

When calculating the cost, we adopt an uncommon strategy. The chromosome encoding scheme only
keeps the mapping between jobs and work groups, and the sequence of orders in the groups. This is
based on an assumption: given the sequence of jobs in a work group, it is easy to calculate the 
optimal time arrangements by shifting the jobs along the timeline. Therefore, the cost of a 
specific arrangement is actually the minimal cost calculated from a sequence of jobs in a workgroup.
