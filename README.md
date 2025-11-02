# Assignment 4 — Smart City / Smart Campus Scheduling
### Topics: Strongly Connected Components (SCC) & Shortest Paths in DAGs  
#### Student: Chepurnenko Sergey
#### Group: SE-2422

---

## Project Goal

This assignment integrates two major algorithmic topics —  
**Strongly Connected Components (SCC)** and **Shortest Paths in Directed Acyclic Graphs (DAG-SP)** —  
in a unified practical case: *“Smart City / Smart Campus Scheduling”*.

The simulated scenario models dependencies among urban or campus service tasks:
- Street cleaning, maintenance, sensor updates, and repairs.
- Some dependencies form **cycles (SCCs)** — to be detected and compressed.
- Others form **acyclic subgraphs (DAGs)** — optimized for scheduling via dynamic programming.

---

## Project Structure

src/  
├─ main/java/  
│ ├─ graph/  
│ │ ├─ scc/ → TarjanSCC, CondensationGraphBuilder  
│ │ ├─ topo/ → TopologicalSort  
│ │ ├─ dagsp/ → ShortestPathDAG, LongestPathDAG  
│ │ ├─ metrics/ → Metrics, AlgorithmMetricsRunner  
│ │ └─ model/ → Graph, Edge  
│ └─ util/ → RandomGraphGenerator, GraphLoader  
├─ test/java/graph/ → GraphAlgorithmsTest  
data_final/ → 9 datasets (small/medium/large)  
metrics/ → recorded performance metrics (.json)  



---

## Dataset Summary

| Dataset | Vertices | Edges | SCCs | Notes |
|----------|-----------|--------|-------|--------|
| **small_1** | 6 | 3 | 6 | Pure DAG (each node separate) |
| **small_2** | 9 | 7 | 7 | Sparse DAG |
| **small_3** | 7 | 5 | 7 | Sparse DAG |
| **medium_1** | 18 | 70 | 3 | Mixed cyclic + DAG |
| **medium_2** | 20 | 82 | 5 | Several SCCs |
| **medium_3** | 15 | 44 | 6 | Dense mixed graph |
| **large_1** | 39 | 523 | 1 | Large connected component |
| **large_2** | 48 | 990 | 1 | Dense large graph |
| **large_3** | 46 | 977 | 1 | Dense graph variant |

🟢 *All datasets were auto-generated to test scaling and density (sparse → dense).  
They include both cyclic and acyclic examples.*

---

## Performance Metrics

| Dataset | Vertices | Edges | SCC Count | SCC Time (ms) | Topo Push | Topo Pop | Shortest (ms) | Longest (ms) |
|----------|-----------|--------|-------------|----------------|------------|-----------|----------------|---------------|
| small_1 | 6 | 3 | 6 | 0.010 | 6 | 6 | 0.009 | 0.007 |
| small_2 | 9 | 7 | 7 | 0.008 | 5 | 5 | 0.012 | 0.008 |
| small_3 | 7 | 5 | 7 | 0.011 | 7 | 7 | 0.014 | 0.020 |
| medium_1 | 18 | 70 | 3 | 0.023 | 0 | 0 | 0.019 | 0.017 |
| medium_2 | 20 | 82 | 5 | 0.038 | 2 | 2 | 0.051 | 0.038 |
| medium_3 | 15 | 44 | 6 | 0.022 | 3 | 3 | 0.017 | 0.016 |
| large_1 | 39 | 523 | 1 | 0.115 | 0 | 0 | 0.334 | 0.366 |
| large_2 | 48 | 990 | 1 | 0.090 | 0 | 0 | 0.173 | 0.201 |
| large_3 | 46 | 977 | 1 | 0.116 | 0 | 0 | 0.207 | 0.208 |

---

## Analysis

### SCC Detection (Tarjan)
- **Complexity:** O(V + E)
- Runs in under **0.12 ms** even for the largest graphs.
- Small graphs (DAG-like) produce multiple single-node SCCs.
- Large graphs show **one giant SCC**, typical for dense directed networks.

**Observation:**  
Tarjan’s method scales linearly with the number of edges — performance grows almost linearly from small to large datasets.

---

### Topological Ordering
- Kahn’s algorithm was used.
- `push/pop` operations scale with number of nodes with indegree = 0.
- For dense graphs (large_*), topological order is not available (graph cyclic).

**Observation:**  
Topo sort confirms DAG validity — for cyclic graphs, `push/pop = 0`, signaling condensation is required.

---

### Shortest Path in DAG
- Implemented via **DP over topological order**.
- Works only on condensed DAGs or pure DAGs.
- Execution time grows slightly with graph size but stays < 0.4 ms.

**Observation:**  
For DAGs with few edges, runtime ≈ O(V + E); for dense graphs, runtime still negligible due to acyclic pruning.

---

### Longest (Critical) Path
- Implemented via sign-inversion DP (max-DP).
- Longest path ≈ *critical chain* of dependent tasks.
- Execution times are close to shortest path timings.

**Observation:**  
Critical path computation is stable and efficient; time differences < 0.05 ms even for large graphs.

---

## Conclusions

| Aspect | Finding |
|---------|----------|
| **SCC Algorithms (Tarjan)** | Best for detecting cyclic dependencies and compressing them before scheduling. Efficient (O(V + E)) and scales linearly. |
| **Topological Ordering (Kahn)** | Suitable only for DAGs; acts as a validator post-SCC condensation. |
| **Shortest Path in DAG** | Optimal for dependency-based scheduling (minimizing total delay). |
| **Longest Path / Critical Path** | Identifies the bottleneck chain of operations (critical sequence). |
| **Performance** | All algorithms execute under 1 ms even on large graphs. Linear scaling validated. |

---

## Practical Recommendations

- Use **Tarjan’s SCC** to preprocess any dependency network (city service, workflow, or campus maintenance).
- Run **Topological Sort** on the condensation DAG to get the correct execution order.
- Apply **DAG Shortest/Longest Path** for optimization -  
  *shortest* for time minimization, *longest* for critical-chain planning.
- Use the **Metrics API** (`System.nanoTime`) to collect reproducible runtime data.

---

## Summary Table — Theory vs Practice

| Algorithm | Theoretical Complexity | Observed Performance | Purpose |
|------------|------------------------|----------------------|----------|
| Tarjan SCC | O(V + E) | < 0.12 ms (all cases) | Detect cycles, build condensation DAG |
| Kahn Topo | O(V + E) | < 0.02 ms (DAG only) | Order tasks after SCC compression |
| DAG Shortest Path | O(V + E) | 0.01–0.33 ms | Schedule optimization (min delay) |
| DAG Longest Path | O(V + E) | 0.01–0.37 ms | Critical-chain identification |

---

## Metrics & Reproducibility

All runtime data are stored in `/metrics/*.json` 