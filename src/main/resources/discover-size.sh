#!/bin/bash

# Calculate and print estimated website size
result="$(\
    grep -o -E "^Length: [0-9]+|saved \[[0-9]+]" "$1" | \
    grep -o -E "[0-9]+" | \
    awk '{sum+=$1} END {printf("%.2f", sum)}'\
)"
echo "$result" >> wget-log
echo "Estimated size: $result bytes"

