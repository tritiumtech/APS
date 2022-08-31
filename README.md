# APS
An Advanced Planning & Scheduling system based on genetic algorithms.

## Chromosome Encoding
Use a 1-dimensional array to represent an arrangement of tasks. Each 
element in the array designates the work group to which
the task is allocated. The actual sequence of tasks in the group is 
determined by a set of rules applied in the cost function.

## Crossover
When doing a crossover, we are effectively swapping the binding between jobs and workgroups.
Consider jobs $\mathbb{A}$, $\mathbb{B}$, $\mathbb{C}$, $\mathbb{D}$, $\mathbb{E}$
```math
Before crossover:

chromesome 1: 0-0-1-1-0
workgrup 0: \mathbb{A} \mathbb{B} \mathbb{E}
workgrup 1: \mathbb{C} \mathbb{D}  

chromesome 1: 1-0-0-1-0
workgrup 0: $\mathbb{B}$ $\mathbb{C}$ $\mathbb{E}$ 
workgrup 1: $\mathbb{A}$ $\mathbb{D}$  

After crossover from position 3 to 4

chromesome 1: 0-0-0-1-0
workgrup 0: $\mathbb{A}$ $\mathbb{B}$ $\mathbb{E}$ 
workgrup 1: $\mathbb{C}$ $\mathbb{D}$  

chromesome 1: 1-0-1-1-0
workgrup 0: $\mathbb{B}$ $\mathbb{E}$ 
workgrup 1: $\mathbb{A}$ $\mathbb{C}$ $\mathbb{D}$  

```
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
