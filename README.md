# 🏗️ Low-Level Design: LRU Cache

A fixed-capacity, generic Least Recently Used (LRU) cache implemented in Java 21. The cache keeps the most recently accessed entries and evicts the least recently used entry when a new key is inserted at capacity. Its core uses an access-ordered `LinkedHashMap` through the `SequencedMap` interface, with synchronized operations for safe concurrent access.

---

## 🎯 1. Requirements & System Constraints

### Functional Requirements
* **Fixed capacity:** Create a cache with a positive maximum capacity.
* **Store and access entries:** Add an element and look it up by element. `SimpleLRUCache<T>` uses each element as both key and value; `ConcurrentLRUCache<K, V>` supports separate keys and values.
* **LRU ordering and eviction:** Successful gets update recency. Inserting a new key into a full cache evicts the least recently used entry.
* **Inspect and reset:** Get a snapshot of cache contents in recency order, check size, and clear all entries.

### Non-Functional Requirements
* **Time complexity:** `get` and `put` are expected O(1) operations with `LinkedHashMap`; `size` and `clear` are O(1) and O(n), respectively. Creating a contents snapshot is O(n).
* **Thread safety:** Each operation on the backing map is synchronized on that map. Individual operations are thread-safe; a sequence of separate operations is not an atomic transaction.
* **Capacity:** Capacity is fixed at construction and must be greater than zero.

---

## 📐 2. Architecture & Design Patterns Used

### 💎 Design Principles
* **Encapsulation:** `SimpleLRUCache<T>` offers a simple element-only API while delegating storage and eviction to `ConcurrentLRUCache<T, T>`.
* **Abstraction:** `ConcurrentLRUCache` exposes `SequencedMap`-based contents as an unmodifiable snapshot. The simple wrapper returns the snapshot's sequenced keys.
* **Separation of responsibilities:** The wrapper defines the element-oriented API; the underlying class manages key/value storage, recency, eviction, and synchronization.

### 🏗️ Design Technique
* **Access-ordered map:** A `LinkedHashMap` configured with `accessOrder = true` moves entries to most-recently-used position on access. The first entry is therefore the eviction candidate.
* **Composition:** `SimpleLRUCache` delegates to `ConcurrentLRUCache` rather than inheriting its implementation.

---

## 🗂️ 3. Class Structure & Core Entities

### Directory Overview
```text
src/main/java/org/rsr007/lrucache/
├── ConcurrentLRUCache.java  # Key/value LRU storage and synchronization
├── SimpleLRUCache.java      # Generic element-only cache facade
└── Application.java         # Demonstration entry point
```

### Key Entities
* `ConcurrentLRUCache<K, V>` — Maintains the fixed-capacity, access-ordered map. `get`, `put`, `size`, `clear`, and snapshot retrieval synchronize on the backing map.
* `SimpleLRUCache<T>` — Convenience API where an element is both its key and value. Supports `add`, `get`, `clear`, and ordered contents retrieval.
* `Application` — Demonstrates access changing recency and insertion triggering eviction.

---

## ⚡ 4. Concurrency & Thread-Safety Analysis
* **Concurrency mechanism:** Each public operation synchronizes on the backing map. This matters for `get` as well as writes because access-order `LinkedHashMap.get` mutates the recency order.
* **Consistent snapshots:** `getAllData()` copies the entries while holding the same lock, then returns an unmodifiable sequenced map. Later cache changes do not change that snapshot.
* **Limits:** Calls are safe individually, but a caller performing multiple calls does not get a lock across the whole sequence. Iteration over the returned snapshot needs no cache lock.

---

## 🚀 5. How to Run & Verify

Requires Java 21 and Maven. From the repository root, compile and run the demonstration:

```bash
mvn clean compile
java -cp target/classes org.rsr007.lrucache.Application
```

The output shows the initial order, the order after accessing `ravi`, and the order after adding `shiva` to a full cache. `ravi` becomes most recently used after access, so `shankar` is evicted when `shiva` is inserted.

Example usage:

```java
SimpleLRUCache<String> cache = new SimpleLRUCache<>(3);
cache.add("ravi");
cache.add("shankar");
cache.add("rai");

String value = cache.get("ravi");
System.out.println(cache.getAllData());
```
