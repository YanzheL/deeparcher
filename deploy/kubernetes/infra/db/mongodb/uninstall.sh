#!/bin/bash
helm uninstall deeparcher-mongodb-sharded && \
kubectl delete pvc datadir-deeparcher-mongodb-sharded-shard0-data-0 && \
kubectl delete pvc datadir-deeparcher-mongodb-sharded-shard1-data-0 && \
kubectl delete pvc datadir-deeparcher-mongodb-sharded-shard2-data-0 && \
kubectl delete pvc datadir-deeparcher-mongodb-sharded-configsvr-0 && \
kubectl delete -f pv.yaml