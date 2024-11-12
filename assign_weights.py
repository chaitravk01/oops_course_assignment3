import random

# Function to add random weights to each edge
def add_weights_to_edges(input_file, output_file, min_capacity=1, max_capacity=100):
    with open(input_file, 'r') as infile, open(output_file, 'w') as outfile:
        for line in infile:
            # Skip comment lines (lines starting with '#')
            if line.startswith('#'):
                outfile.write(line)
                continue
            
            # Split the edge data into FromNodeId and ToNodeId
            from_node, to_node = line.split()
            
            # Generate a random weight (capacity) for the edge between nodes
            capacity = random.randint(min_capacity, max_capacity)
            
            # Write the edge and its capacity to the output file
            outfile.write(f"{from_node}\t{to_node}\t{capacity}\n")

# Input and Output file paths
input_file = 'roadNet-CA.txt'  # Path to the input file
output_file = 'roadNet-CA-with-weights.txt'  # Path to the output file with weights

# Call the function to add weights
add_weights_to_edges(input_file, output_file)

print(f"File with weights saved to: {output_file}")
