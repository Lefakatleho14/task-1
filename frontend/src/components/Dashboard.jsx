import React from 'react'
import { useQuery } from 'react-query'
import api from '../services/api'
import { FaUserGraduate, FaChalkboardTeacher, FaBook, FaChartLine } from 'react-icons/fa'

function Dashboard() {
  const { data: stats, isLoading } = useQuery('dashboardStats', async () => {
    const response = await api.get('/dashboard/stats')
    return response.data
  })

  const statCards = [
    {
      title: 'Total Students',
      value: stats?.total_students || 0,
      icon: FaUserGraduate,
      color: 'bg-blue-500',
    },
    {
      title: 'Total Teachers',
      value: stats?.total_teachers || 0,
      icon: FaChalkboardTeacher,
      color: 'bg-green-500',
    },
    {
      title: 'Total Courses',
      value: stats?.total_courses || 0,
      icon: FaBook,
      color: 'bg-purple-500',
    },
    {
      title: 'Active Sessions',
      value: '12',
      icon: FaChartLine,
      color: 'bg-orange-500',
    },
  ]

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-full">
        <div className="text-gray-600">Loading...</div>
      </div>
    )
  }

  return (
    <div>
      <h2 className="text-2xl font-bold text-gray-800 mb-6">Dashboard</h2>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        {statCards.map((stat, index) => {
          const Icon = stat.icon
          return (
            <div
              key={index}
              className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow"
            >
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-gray-500 text-sm font-medium">{stat.title}</p>
                  <p className="text-3xl font-bold text-gray-800 mt-2">{stat.value}</p>
                </div>
                <div className={`${stat.color} p-4 rounded-lg`}>
                  <Icon className="text-white text-2xl" />
                </div>
              </div>
            </div>
          )
        })}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="bg-white rounded-lg shadow-md p-6">
          <h3 className="text-lg font-semibold text-gray-800 mb-4">Recent Activities</h3>
          <div className="space-y-4">
            {[1, 2, 3, 4].map((item) => (
              <div key={item} className="flex items-start space-x-3 p-3 bg-gray-50 rounded-lg">
                <div className="w-2 h-2 bg-primary-500 rounded-full mt-2"></div>
                <div>
                  <p className="text-sm font-medium text-gray-800">
                    New student enrolled
                  </p>
                  <p className="text-xs text-gray-500">2 hours ago</p>
                </div>
              </div>
            ))}
          </div>
        </div>

        <div className="bg-white rounded-lg shadow-md p-6">
          <h3 className="text-lg font-semibold text-gray-800 mb-4">Quick Actions</h3>
          <div className="grid grid-cols-2 gap-4">
            <button className="p-4 bg-primary-50 hover:bg-primary-100 rounded-lg text-primary-700 font-medium transition-colors">
              Add Student
            </button>
            <button className="p-4 bg-green-50 hover:bg-green-100 rounded-lg text-green-700 font-medium transition-colors">
              Add Teacher
            </button>
            <button className="p-4 bg-purple-50 hover:bg-purple-100 rounded-lg text-purple-700 font-medium transition-colors">
              Create Course
            </button>
            <button className="p-4 bg-orange-50 hover:bg-orange-100 rounded-lg text-orange-700 font-medium transition-colors">
              Mark Attendance
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}

export default Dashboard
