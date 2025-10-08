#!/bin/bash

echo "Starting School Management System Backend..."
echo "==========================================="
echo ""

# Activate virtual environment if it exists
if [ -d "venv" ]; then
    source venv/bin/activate
fi

# Start the backend server
python3 -m uvicorn backend.main:app --host 0.0.0.0 --port 8000 --reload

echo ""
echo "Backend server stopped."
