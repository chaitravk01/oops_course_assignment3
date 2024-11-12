import random

# Function to generate dummy edge data with weights
def generate_edges_with_weights(file_path, num_edges=50, num_nodes=25):
    with open(file_path, 'w') as f:
        # Write the header with dummy information
        f.write("# Directed graph (each unordered pair of nodes is saved once): roadNet-CA.txt\n")
        f.write("# California road network\n")
        f.write(f"# Nodes: {num_nodes} Edges: {num_edges}\n")
        f.write("# FromNodeId\tToNodeId\tWeight\n")
        
        # Generate the edges and write to the file
        for _ in range(num_edges):
            node1 = random.randint(0, num_nodes - 1)
            node2 = random.randint(0, num_nodes - 1)
            while node1 == node2:  # Make sure it's not a self-loop
                node2 = random.randint(0, num_nodes - 1)
            weight = random.randint(1, 100)  # Random weight between 1 and 100
            f.write(f"{node1}\t{node2}\t{weight}\n")
    print(f"Edge data with weights written to {file_path}")

# File path for the generated file
edge_file = "roadNet-CA-with-weights(dummy).txt"

# Generate dummy edge data with weights
generate_edges_with_weights(edge_file, num_edges=50, num_nodes=25)
