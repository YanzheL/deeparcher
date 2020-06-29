#!/bin/bash
helm upgrade -i kafka-operator strimzi/strimzi-kafka-operator -f values.yaml -n production