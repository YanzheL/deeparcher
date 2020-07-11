#!/usr/bin/env bash
helm upgrade -i deeparcher-mariadb bitnami/mariadb -f values.yaml
