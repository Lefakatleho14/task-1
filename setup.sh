#!/bin/bash

# School Management System Setup Script

echo "========================================="
echo "School Management System - Setup"
echo "========================================="
echo ""

# Check if Python is installed
if ! command -v python3 &> /dev/null
then
    echo "❌ Python 3 is not installed. Please install Python 3.8 or higher."
    exit 1
fi

echo "✓ Python 3 found"

# Check if Node.js is installed
if ! command -v node &> /dev/null
then
    echo "❌ Node.js is not installed. Please install Node.js 16 or higher."
    exit 1
fi

echo "✓ Node.js found"

# Backend Setup
echo ""
echo "📦 Setting up Backend..."
echo "------------------------"

# Create virtual environment
if [ ! -d "venv" ]; then
    echo "Creating virtual environment..."
    python3 -m venv venv
fi

# Activate virtual environment
source venv/bin/activate

# Install Python dependencies
echo "Installing Python dependencies..."
pip install -q --upgrade pip
pip install -q -r requirements.txt

if [ $? -eq 0 ]; then
    echo "✓ Backend dependencies installed"
else
    echo "❌ Failed to install backend dependencies"
    exit 1
fi

# Initialize database
echo ""
echo "🗄️  Initializing database..."
python3 init_db.py

if [ $? -eq 0 ]; then
    echo "✓ Database initialized"
else
    echo "❌ Failed to initialize database"
    exit 1
fi

# Frontend Setup
echo ""
echo "📦 Setting up Frontend..."
echo "------------------------"

cd frontend

if [ ! -d "node_modules" ]; then
    echo "Installing Node.js dependencies..."
    npm install
    
    if [ $? -eq 0 ]; then
        echo "✓ Frontend dependencies installed"
    else
        echo "❌ Failed to install frontend dependencies"
        exit 1
    fi
else
    echo "✓ Frontend dependencies already installed"
fi

cd ..

echo ""
echo "========================================="
echo "✅ Setup Complete!"
echo "========================================="
echo ""
echo "To start the application:"
echo ""
echo "1. Start the backend server:"
echo "   ./start_backend.sh"
echo ""
echo "2. In a new terminal, start the frontend:"
echo "   ./start_frontend.sh"
echo ""
echo "Default login credentials:"
echo "  Username: admin"
echo "  Password: admin123"
echo ""
echo "========================================="
