# Logic

A deduction engine implementing Aristotelian syllogistics and Brusentsov's ternary logic.

Logic operates on categorical propositions (A, E, I, O) and derives conclusions through classical syllogistic figures such as BARBARA, CELARENT, CESARE and CAMESTRES.

The engine is able to explain its conclusions by providing the exact syllogistic rule used during deduction.

---

## Example

```
> A: irish -> Ireland
-> Accepted

> E: dragon -> Ireland
-> Accepted

> ?: irish -> dragon
-> Found: E: irish -> dragon :: CAMESTRES

> ?: dragon -> irish
-> Found: E: dragon -> irish :: CESARE
```

Meaning:

- All Irish are in Ireland.
- No dragons are in Ireland.

Therefore:

- No Irish are dragons.

The engine not only produces a conclusion but also identifies the syllogistic figure used to derive it.

---

## Features

- Aristotelian syllogistic reasoning
- A, E, I, O categorical propositions
- Explainable deductions
- Proof tracing
- Deduction chains
- Brusentsov-style ternary semantics
- Java API and interactive console

---

## Logic Alphabet

The engine operates with three logical values:

| Symbol | Meaning |
|----------|----------|
| T | True |
| U | Unknown / Possible |
| F | False |

Unlike binary logic, conclusions may remain uncertain when available knowledge is insufficient.

---

## Supported Relations

| Relation | Meaning |
|-----------|---------|
| A | All S are P |
| E | No S are P |
| I | Some S are P |
| O | Some S are not P |

Extended relations are also supported.

---

## Supported Syllogistic Modes

| Figure 1 | Figure 2 | Figure 3 | Figure 4 |
|----------|----------|----------|----------|
| BARBARA | CESARE | DARAPTI | BAMALIP |
| CELARENT | CAMESTRES | DISAMIS | CALEMES |
| DARII | FESTINO | DATISI | DIMATIS |
| FERIO | BAROCO | FELAPTON | FESAPO |
| | | BOCARDO | FRESISON |
| | | FERISON | |

---

## Java Example

```java
Engine engine = Engine.create();

// All humans are mortal
engine.fact(Fact.of("humans", "mortals", Relation.A()));

// All Socrates are humans
engine.fact(Fact.of("Socrates", "humans", Relation.A()));

var result = engine.find("Socrates", "mortals");

if (result.isPresent()) {
    Deduction deduction = result.get();
    System.out.println("Deduction found!");
}
```

---

## Why?

Most modern reasoning systems are based on binary true/false evaluation.

Logic explores an alternative approach inspired by Aristotelian reasoning and Brusentsov's interpretation of ternary logic, where a proposition may be true, false, or remain possible/undetermined.

This makes it possible to perform formal deductions while preserving uncertainty instead of collapsing every statement into a binary decision.

---

## Console

You can use the interactive console to explore deduction chains and reasoning paths.

```
=== Logic Console ===
Logic alphabet: FALSE=F, UNKNOWN=U, TRUE=T
Relations:
A: S -> P   - means: `All S are P`
E: S -> P   - means: `No S are P`
I: S -> P   - means: `Some S are P`
O: S -> P   - means: `Some S are not P`
 ----
 help                                - this screen
 save <file.ext>                     - save session
 load <file.ext>                     - load session
 exit                                - exit
 REL: S -> P                         - add fact (REL: A, E, I, O, A', !A, !A' etc.)
 ? : S -> P                          - query deduction (example: ?: greeks -> mortals)
 ----
> A: people -> mortals
-> Accepted
> I: people -> irish
-> Accepted
> A: irish -> Irland
-> Accepted
> E: dragon -> Irland
-> Accepted
> ?: mortals -> dragon
-> Found: O: mortals -> dragon :: AOO
> ?: people -> dragon
-> Found: O: people -> dragon :: AOO
> ?: dragon -> people
-> Found: O: dragon -> people :: OAO
> ?: people -> Irland
-> Found: I: people -> Irland :: DARII
> exit
```

---

## License

MIT License
