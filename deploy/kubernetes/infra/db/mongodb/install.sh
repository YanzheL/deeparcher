#!/bin/bash
helm upgrade -i deeparcher-mongodb-sharded bitnami/mongodb-sharded -f values.yaml