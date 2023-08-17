#!/bin/bash

# Check if second argument is provided
if [ -z "$2" ]
then
    # Calculate and print estimated website size
    result="$(\
        grep -o -E "^Length: [0-9]+|saved \[[0-9]+]" "$1" | \
        grep -o -E "[0-9]+" | \
        awk '{sum+=$1} END {printf("%.2f", sum)}'\
    )"
    echo "$result" >> wget-log
    echo "Estimated size: $result bytes"
else
    # Calculate and print estimated size of files
    echo "Estimated size of $2 files: $(\
        grep -e "Length" "$1" | grep -e "$2" | grep -v "unspecified" | \
        awk '{sum+=$2} END {printf("%.2f", sum)}'\
    ) bytes"
fi

exit 0
