# APS
An Advanced Planning & Scheduling system based on genetic algorithms.
基于遗传算法的任务高级排程系统

## Chromosome Encoding 染色体编码
Use a 1-dimensional array to represent an arrangement of tasks. Each 
element in the array designates the work group to which
the task is allocated. The actual sequence of tasks in the group is 
determined by a set of rules applied in the cost function.
用一个一维数组来表达一个任务分配计划。每个数组里的元素代表一个开发小组。这个小组的实际任务顺序则由cost函数来决定。这种编码方式是降低计算维度的关键。

## Crossover 杂交
When doing a crossover, we are effectively swapping the binding between jobs and workgroups.
Consider jobs $\mathbb{A}$, $\mathbb{B}$, $\mathbb{C}$, $\mathbb{D}$, $\mathbb{E}$
在做杂交动作时，我们其实是在对换任务和开发小组的关联关系。考虑任务A、B、C、D、E
> Before crossover:
在杂交之前：
- chromesome 0: 0-0-1-1-0

  - workgroup 0: $\mathbb{A}$ $\mathbb{B}$ $\mathbb{E}$
  - workgroup 1: $\mathbb{C}$ $\mathbb{D}$

- chromesome 1: 1-0-0-1-0
  - workgroup 0: $\mathbb{B}$ $\mathbb{C}$ $\mathbb{E}$ 
  - workgroup 1: $\mathbb{A}$ $\mathbb{D}$  

> After crossover from position 3 to 4

- chromesome 2: 0-0-0-1-0
  - workgroup 0: $\mathbb{A}$ $\mathbb{B}$ $\mathbb{C}$ $\mathbb{E}$ 
  - workgroup 1: $\mathbb{D}$  

- chromesome 3: 1-0-1-1-0
  - workgroup 0: $\mathbb{A}$ $\mathbb{C}$ $\mathbb{D}$
  - workgroup 1: $\mathbb{B}$ $\mathbb{E}$

## Mutation
There are two types of mutations:
1. Swapping two sequences of jobs between compatible work groups

> Before tpye-A mutation

- chromesome 0: 0-0-0-1-0
    - workgroup 0: $\mathbb{A}$ $\mathbb{B}$ $\mathbb{C}$
    - workgroup 1: $\mathbb{D}$ $\mathbb{E}$
    - 
> After type-A mutation

- chromesome 0-A: 1-0-1-1-0
    - workgroup 0: $\mathbb{A}$ $\mathbb{E}$
    - workgroup 1: $\mathbb{D}$ $\mathbb{B}$ $\mathbb{C}$

2. Moving one or more tasks from one group to another (1-directional and without swapping)

> Before type-B mutation

- chromesome 0: 0-0-0-1-0
    - workgroup 0: $\mathbb{A}$ $\mathbb{B}$ $\mathbb{C}$
    - workgroup 1: $\mathbb{D}$ $\mathbb{E}$

> After type-B mutation

- chromesome 0-B: 0-0-0-1-0
  - workgroup 0: $\mathbb{A}$ $\mathbb{B}$
  - workgroup 1: $\mathbb{D}$ $\mathbb{E}$ $\mathbb{C}$

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

# System Workflow
The workflow is implemented in the IMS class. Each epoch comprises three steps:
- populate() This step generates an initial population of chromosomes (Arrangment instances). The
cost of each arrangement is calculated after each instance gets generated.
- proliferate() This step does crossovers and mutations based on the initial population
  - fitness scores. The fitness scores are calculated based on the initial scores, after linear
  formation into positive numbers
  - rollthewheel(). Chromosomes participate in crossover or mutations based on their fitness. Higher
  fitness means higher chance of being selected
- eliminate() This step removes unfit instances and keeps only the best ones.
