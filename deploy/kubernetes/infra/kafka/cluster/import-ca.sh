#!/bin/bash
kubectl create secret generic kafka-cluster-ca-cert --from-file=ca.crt=cluster-ca.crt
kubectl create secret generic kafka-cluster-ca --from-file=ca.key=cluster-ca.key
kubectl create secret generic kafka-clients-ca-cert --from-file=ca.crt=cluster-ca.crt
kubectl create secret generic kafka-clients-ca --from-file=ca.key=cluster-ca.key

kubectl label secret kafka-cluster-ca-cert strimzi.io/kind=Kafka strimzi.io/cluster=kafka
kubectl label secret kafka-cluster-ca strimzi.io/kind=Kafka strimzi.io/cluster=kafka
kubectl label secret kafka-clients-ca-cert strimzi.io/kind=Kafka strimzi.io/cluster=kafka
kubectl label secret kafka-clients-ca strimzi.io/kind=Kafka strimzi.io/cluster=kafka