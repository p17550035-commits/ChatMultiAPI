================================================================
=                     BEGIN FILE: core_network.md              =
================================================================
| VERSION: 1.0                                                  |
| FORMAT: MACHINE-READABLE SPECIFICATION                        |
| META:                                                         |
|   author: Peter + Copilot                                     |
|   system: Frankenstein Meta-OS                                |
|   module: Router/Hub/Redundancy Unified Layer                 |
|   layer: Core Circulatory + Fault-Tolerance Mesh              |
|   last_updated: 2026-07-08T11:35:00-04:00                     |
|   ml_tags: router, hub, redundancy, heartbeat, logging,       |
|            breaker, switch, alarms, closed_loop, meta_os      |
----------------------------------------------------------------
| SECTION 1 — PURPOSE                                           |
----------------------------------------------------------------
| This subsystem defines the three critical components that     |
| keep the Meta-OS alive:                                       |
|   1. Router Layer                                              |
|   2. Hub Layer                                                 |
|   3. Redundancy Layer                                          |
|                                                                |
| These layers share:                                            |
|   - a unified heartbeat mesh                                   |
|   - a unified logging mesh                                     |
|   - a unified alarm mesh                                       |
|   - a unified breaker/switch mesh                              |
|                                                                |
| If ANY layer fails, the others detect it and compensate.       |
| This creates a self-healing, self-routing, fault-tolerant OS. |
----------------------------------------------------------------
| SECTION 2 — SHARED HEARTBEAT MESH                             |
----------------------------------------------------------------
| All three layers (Router, Hub, Redundancy) share a heartbeat: |
|                                                                |
|   Router → Hub → Redundancy → Router                           |
|                                                                |
| Normal mode:                                                   |
|   - Router listens for module heartbeats                       |
|   - Hub listens for router heartbeats                          |
|   - Redundancy listens for hub heartbeats                      |
|                                                                |
| Failure mode:                                                  |
|   - Mesh switches to bi-directional heartbeat scanning         |
|   - Every layer pings every other layer                        |
|   - Missing heartbeat triggers breaker/switch logic            |
|                                                                |
| This prevents silent failures and isolates dead components.    |
----------------------------------------------------------------
| SECTION 3 — ROUTER LAYER                                      |
----------------------------------------------------------------
| Responsibilities:                                              |
|   - Receive module heartbeats                                  |
|   - Validate packets                                           |
|   - Timestamp arrivals                                         |
|   - Detect missed intervals                                    |
|   - Forward status to Hub                                      |
|   - Log all events                                             |
|   - Emit router heartbeat to Hub + Redundancy                  |
|                                                                |
| Router Logs:                                                   |
|   - heartbeat_received                                         |
|   - heartbeat_missed                                           |
|   - validation_error                                           |
|   - routing_decision                                           |
|   - breaker_triggered                                          |
|   - alarm_triggered                                            |
----------------------------------------------------------------
| SECTION 4 — HUB LAYER                                         |
----------------------------------------------------------------
| Responsibilities:                                              |
|   - Track module + router heartbeat status                     |
|   - Maintain layer health map                                  |
|   - Trigger redundancy switching                               |
|   - Trigger alarms                                             |
|   - Trigger calendar scheduling                                |
|   - Emit hub heartbeat to Router + Redundancy                  |
|                                                                |
| Hub Logs:                                                      |
|   - status_change                                              |
|   - redundancy_switch                                           |
|   - reconnection_attempt                                       |
|   - recovery_success                                           |
|   - recovery_failure                                           |
|   - breaker_event                                              |
|   - alarm_event                                                |
----------------------------------------------------------------
| SECTION 5 — REDUNDANCY LAYER                                  |
----------------------------------------------------------------
| Responsibilities:                                              |
|   - Monitor hub + router heartbeats                            |
|   - Take over routing during primary failure                   |
|   - Perform reconnection pings                                 |
|   - Flip breaker/switch lines                                  |
|   - Emit redundancy heartbeat to Router + Hub                  |
|                                                                |
| Redundancy Logs:                                               |
|   - takeover_event                                             |
|   - ping_result                                                |
|   - recovery_detected                                          |
|   - fallback_routing                                           |
|   - breaker_flip                                               |
|   - alarm_triggered                                            |
----------------------------------------------------------------
| SECTION 6 — BREAKER / SWITCH MESH                              |
----------------------------------------------------------------
| Triggered when:                                                |
|   - Router heartbeat stops                                     |
|   - Hub heartbeat stops                                        |
|   - Redundancy heartbeat stops                                 |
|                                                                |
| Behavior:                                                      |
|   - Switch from primary → secondary routing line               |
|   - Attempt reconnection                                       |
|   - Log breaker event                                          |
|   - Trigger alarms                                             |
|   - Notify user                                                |
|   - Schedule recovery window                                   |
----------------------------------------------------------------
| SECTION 7 — ALARM SYSTEM                                       |
----------------------------------------------------------------
| Alarms triggered by:                                           |
|   - missed heartbeat                                           |
|   - breaker flip                                               |
|   - redundancy takeover                                        |
|   - recovery detection                                         |
|                                                                |
| Alarm outputs:                                                 |
|   - text message                                               |
|   - email                                                      |
|   - push notification                                          |
|   - emergency alert                                            |
|   - optional automated call                                    |
----------------------------------------------------------------
| SECTION 8 — SHARED LOGGING MESH                                |
----------------------------------------------------------------
| All three layers write logs with shared correlation_id:        |
|                                                                |
|   router_log/<id>.log                                          |
|   hub_log/<id>.log                                             |
|   redundancy_log/<id>.log                                      |
|                                                                |
| This allows toaster-LLMs to debug the entire failure chain.    |
----------------------------------------------------------------
| SECTION 9 — SUMMARY                                            |
----------------------------------------------------------------
| The unified Router + Hub + Redundancy subsystem provides:      |
|   - fault tolerance                                            |
|   - self-healing                                               |
|   - self-routing                                               |
|   - self-monitoring                                            |
|   - self-notifying                                             |
|   - bi-directional heartbeat mesh                              |
|   - shared logging                                             |
|   - shared alarms                                              |
|   - shared breaker/switch logic                                |
|                                                                |
| This is the circulatory + nervous system of the Meta-OS.       |
----------------------------------------------------------------
| FOOTER                                                         |
----------------------------------------------------------------
| FILE: core_network
