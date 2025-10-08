import React, { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from 'react-query'
import api from '../services/api'
import { FaPlus, FaEdit, FaTrash, FaSearch } from 'react-icons/fa'

function Courses() {
  const queryClient = useQueryClient()
  const [showModal, setShowModal] = useState(false)
  const [editingCourse, setEditingCourse] = useState(null)
  const [searchTerm, setSearchTerm] = useState('')
  const [formData, setFormData] = useState({
    course_name: '',
    course_code: '',
    description: '',
    teacher_id: '',
    grade_level: 1,
    section: 'A',
    schedule: '',
    room_number: '',
    max_students: 40,
  })

  const { data: courses, isLoading: coursesLoading } = useQuery('courses', async () => {
    const response = await api.get('/courses')
    return response.data
  })

  const { data: teachers, isLoading: teachersLoading } = useQuery('teachers', async () => {
    const response = await api.get('/teachers')
    return response.data
  })

  const createMutation = useMutation(
    (data) => api.post('/courses', data),
    {
      onSuccess: () => {
        queryClient.invalidateQueries('courses')
        setShowModal(false)
        resetForm()
      },
    }
  )

  const updateMutation = useMutation(
    ({ id, data }) => api.put(`/courses/${id}`, data),
    {
      onSuccess: () => {
        queryClient.invalidateQueries('courses')
        setShowModal(false)
        setEditingCourse(null)
        resetForm()
      },
    }
  )

  const resetForm = () => {
    setFormData({
      course_name: '',
      course_code: '',
      description: '',
      teacher_id: '',
      grade_level: 1,
      section: 'A',
      schedule: '',
      room_number: '',
      max_students: 40,
    })
  }

  const handleSubmit = (e) => {
    e.preventDefault()
    const data = {
      ...formData,
      teacher_id: parseInt(formData.teacher_id),
    }
    if (editingCourse) {
      updateMutation.mutate({ id: editingCourse.id, data })
    } else {
      createMutation.mutate(data)
    }
  }

  const handleEdit = (course) => {
    setEditingCourse(course)
    setFormData({
      course_name: course.course_name,
      description: course.description || '',
      teacher_id: course.teacher_id.toString(),
      grade_level: course.grade_level,
      section: course.section || 'A',
      schedule: course.schedule || '',
      room_number: course.room_number || '',
      max_students: course.max_students || 40,
    })
    setShowModal(true)
  }

  const getTeacherName = (teacherId) => {
    const teacher = teachers?.find(t => t.id === teacherId)
    return teacher ? `${teacher.first_name} ${teacher.last_name}` : 'N/A'
  }

  const filteredCourses = courses?.filter((course) =>
    `${course.course_name} ${course.course_code}`
      .toLowerCase()
      .includes(searchTerm.toLowerCase())
  )

  if (coursesLoading || teachersLoading) {
    return <div className="flex items-center justify-center h-full">Loading...</div>
  }

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-bold text-gray-800">Courses</h2>
        <button
          onClick={() => {
            resetForm()
            setEditingCourse(null)
            setShowModal(true)
          }}
          className="bg-primary-500 text-white px-4 py-2 rounded-lg flex items-center space-x-2 hover:bg-primary-600 transition-colors"
        >
          <FaPlus />
          <span>Add Course</span>
        </button>
      </div>

      <div className="bg-white rounded-lg shadow-md p-6">
        <div className="mb-4">
          <div className="relative">
            <FaSearch className="absolute left-3 top-3 text-gray-400" />
            <input
              type="text"
              placeholder="Search courses..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent"
            />
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredCourses?.map((course) => (
            <div key={course.id} className="bg-gray-50 rounded-lg p-6 hover:shadow-lg transition-shadow">
              <div className="flex justify-between items-start mb-4">
                <div>
                  <h3 className="text-lg font-semibold text-gray-800">{course.course_name}</h3>
                  <p className="text-sm text-gray-500">{course.course_code}</p>
                </div>
                <div className="flex space-x-2">
                  <button
                    onClick={() => handleEdit(course)}
                    className="text-primary-600 hover:text-primary-900"
                  >
                    <FaEdit />
                  </button>
                </div>
              </div>
              <div className="space-y-2 text-sm">
                <p><span className="font-medium">Teacher:</span> {getTeacherName(course.teacher_id)}</p>
                <p><span className="font-medium">Grade:</span> {course.grade_level} - {course.section}</p>
                <p><span className="font-medium">Room:</span> {course.room_number || 'N/A'}</p>
                <p><span className="font-medium">Max Students:</span> {course.max_students}</p>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Modal */}
      {showModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-lg p-6 max-w-2xl w-full max-h-[90vh] overflow-y-auto">
            <h3 className="text-xl font-bold mb-4">
              {editingCourse ? 'Edit Course' : 'Add Course'}
            </h3>
            <form onSubmit={handleSubmit} className="space-y-4">
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Course Name *
                  </label>
                  <input
                    type="text"
                    required
                    value={formData.course_name}
                    onChange={(e) => setFormData({ ...formData, course_name: e.target.value })}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500"
                  />
                </div>
                {!editingCourse && (
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Course Code *
                    </label>
                    <input
                      type="text"
                      required
                      value={formData.course_code}
                      onChange={(e) => setFormData({ ...formData, course_code: e.target.value })}
                      className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500"
                    />
                  </div>
                )}
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Teacher *
                  </label>
                  <select
                    required
                    value={formData.teacher_id}
                    onChange={(e) => setFormData({ ...formData, teacher_id: e.target.value })}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500"
                  >
                    <option value="">Select Teacher</option>
                    {teachers?.map((teacher) => (
                      <option key={teacher.id} value={teacher.id}>
                        {teacher.first_name} {teacher.last_name}
                      </option>
                    ))}
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Grade Level *
                  </label>
                  <input
                    type="number"
                    required
                    min="1"
                    max="12"
                    value={formData.grade_level}
                    onChange={(e) => setFormData({ ...formData, grade_level: parseInt(e.target.value) })}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Section
                  </label>
                  <input
                    type="text"
                    value={formData.section}
                    onChange={(e) => setFormData({ ...formData, section: e.target.value })}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Room Number
                  </label>
                  <input
                    type="text"
                    value={formData.room_number}
                    onChange={(e) => setFormData({ ...formData, room_number: e.target.value })}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Max Students
                  </label>
                  <input
                    type="number"
                    min="1"
                    value={formData.max_students}
                    onChange={(e) => setFormData({ ...formData, max_students: parseInt(e.target.value) })}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Schedule
                  </label>
                  <input
                    type="text"
                    placeholder="e.g., Mon/Wed/Fri 10:00-11:00"
                    value={formData.schedule}
                    onChange={(e) => setFormData({ ...formData, schedule: e.target.value })}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500"
                  />
                </div>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Description
                </label>
                <textarea
                  value={formData.description}
                  onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                  rows="3"
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500"
                />
              </div>
              <div className="flex justify-end space-x-3">
                <button
                  type="button"
                  onClick={() => {
                    setShowModal(false)
                    setEditingCourse(null)
                    resetForm()
                  }}
                  className="px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="px-4 py-2 bg-primary-500 text-white rounded-lg hover:bg-primary-600"
                >
                  {editingCourse ? 'Update' : 'Create'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}

export default Courses
