# Create folder
mkdir -p sql

# Create init.sql file
cat > sql/init.sql << 'EOF'
-- Initialize database
CREATE DATABASE IF NOT EXISTS needledrop;
USE needledrop;
EOF