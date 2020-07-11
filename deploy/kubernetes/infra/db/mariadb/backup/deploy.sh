#!/usr/bin/env bash
helm upgrade -i deeparcher-db-backup-job stable/mysqldump -f values.yaml
