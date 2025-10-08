import React, { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from 'react-query'
import api from '../services/api'
import { FaPlus, FaTrophy } from 'react-icons/fa'

function Grades() {
  const queryClient = useQueryClient()
  const [showModal, setShowModal] = useState(false)
  const [selectedStudent, setSelectedStudent] = useState('')
  const [selectedCourse, setSelectedCourse] = useState('')
  const [formData, setFormData] = useState({
    student_id: '',
    course_id: '',
    assessment_type: 'exam',
    assessment_name: '',
    max_marks: 100,
    obtained_marks: 0,
    grade: 'A',
    assessment_date: new Date().toISOString().split('T')[0],
    remarks: '',
  })

  const { data: students } = useQuery('students', async () => {
    const response = await api.get('/students')
    return response.data
  })

  const { data: courses } = useQuery('courses', async () => {
    const response = await api.get('/courses')
    return response.data
  })

  const { data: grades, isLoading } = useQuery(
    ['grades', selectedStudent, selectedCourse],
    async () => {
      const params = new URLSearchParams()
      if (selectedStudent) params.append('student_id', selectedStudent)
      if (selectedCourse) params.append('course_id', selectedCourse)
      const response = await api.get(`/grades?${params.toString()}`)
      return response.data
    }
  )

  const createMutation = useMutation(
    (data) => api.post('/grades', data),
    {
      onSuccess: () => {
        queryClient.invalidateQueries('grades')
        setShowModal(false)
        resetForm()
      },
    }
  )

  const resetForm = () => {
    setFormData({
      student_id: '',
      course_id: '',
      assessment_type: 'exam',
      assessment_name: '',
      max_marks: 100,
      obtained_marks: 0,
      grade: 'A',
      assessment_date: new Date().toISOString().split('T')[0],
      remarks: '',
    })
  }

  const handleSubmit = (e) => {
    e.preventDefault()
    const data = {
      ...formData,
      student_id: parseInt(formData.student_id),
      course_id: parseInt(formData.course_id),
      max_marks: parseFloat(formData.max_marks),
      obtained_marks: parseFloat(formData.obtained_marks),
    }
    createMutation.mutate(data)
  }

  const getStudentName = (studentId) => {
    const student = students?.find(s => s.id === studentId)
    return student ? `${student.first_name} ${student.last_name}` : 'N/A'
  }

  const getCourseName = (courseId) => {
    const course = courses?.find(c => c.id === courseId)
    return course ? course.course_name : 'N/A'
  }

  const getGradeColor = (grade) => {
    switch (grade) {
      case 'A':
      case 'A+':
        return 'bg-green-100 text-green-800'
      case 'B':
      case 'B+':
        return 'bg-blue-100 text-blue-800'
      case 'C':
      case 'C+':
        return 'bg-yellow-100 text-yellow-800'
      case 'D':
        return 'bg-orange-100 text-orange-800'
      case 'F':
        return 'bg-red-100 text-red-800'
      default:
        return 'bg-gray-100 text-gray-800'
    }
  }

  if (isLoading) {
    return <div className="flex items-center justify-center h-full">Loading...</div>
  }

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-bold text-gray-800">Grades</h2>
        <button
          onClick={() => {
            resetForm()
            setShowModal(true)
          }}
          className="bg-primary-500 text-white px-4 py-2 rounded-lg flex items-center space-x-2 hover:bg-primary-600 transition-colors"
        >
          <FaPlus />
          <span>Add Grade</span>
        </button>
      </div>

      <div className="bg-white rounded-lg shadow-md p-6 mb-6">
        <h3 className="text-lg font-semibold mb-4">Filters</h3>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Filter by Student
            </label>
            <select
              value={selectedStudent}
              onChange={(e) => setSelectedStudent(e.target.value)}
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500"
            >
              <option value="">All Students</option>
              {students?.map((student) => (
                <option key={student.id} value={student.id}>
                  {student.first_name} {student.last_name} - {student.roll_number}
                </option>
              ))}
            </select>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Filter by Course
            </label>
            <select
              value={selectedCourse}
              onChange={(e) => setSelectedCourse(e.target.value)}
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500"
            >
              <option value="">All Courses</option>
              {courses?.map((course) => (
                <option key={course.id} value={course.id}>
                  {course.course_name} - {course.course_code}
                </option>
              ))}
            </select>
          </div>
        </div>
      </div>

      <div className="bg-white rounded-lg shadow-md p-6">
        <h3 className="text-lg font-semibold mb-4">Grade Records</h3>
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Student
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Course
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Assessment
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Type
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Marks
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Grade
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Date
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {grades?.map((record) => (
                <tr key={record.id} className="hover:bg-gray-50">
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {getStudentName(record.student_id)}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {getCourseName(record.course_id)}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {record.assessment_name}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 capitalize">
                    {record.assessment_type}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {record.obtained_marks} / {record.max_marks}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span
                      className={`px-3 py-1 inline-flex text-xs leading-5 font-semibold rounded-full ${getGradeColor(
                        record.grade
                      )}`}
                    >
                      <FaTrophy className="mr-1" />
                      {record.grade}
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {record.assessment_date}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          {grades?.length === 0 && (
            <div className="text-center py-8 text-gray-500">
              No grade records found
            </div>
          )}
        </div>
      </div>

      {/* Modal */}
      {showModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-lg p-6 max-w-md w-full max-h-[90vh] overflow-y-auto">
            <h3 className="text-xl font-bold mb-4">Add Grade</h3>
            <form onSubmit={handleSubmit} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Student *
                </label>
                <select
                  required
                  value={formData.student_id}
                  onChange={(e) => setFormData({ ...formData, student_id: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500"
                >
                  <option value="">Select Student</option>
                  {students?.map((student) => (
                    <option key={student.id} value={student.id}>
                      {student.first_name} {student.last_name} - {student.roll_number}
                    </option>
                  ))}
                </select>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Course *
                </label>
                <select
                  required
                  value={formData.course_id}
                  onChange={(e) => setFormData({ ...formData, course_id: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500"
                >
                  <option value="">Select Course</option>
                  {courses?.map((course) => (
                    <option key={course.id} value={course.id}>
                      {course.course_name}
                    </option>
                  ))}
                </select>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Assessment Type *
                </label>
                <select
                  required
                  value={formData.assessment_type}
                  onChange={(e) => setFormData({ ...formData, assessment_type: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500"
                >
                  <option value="quiz">Quiz</option>
                  <option value="exam">Exam</option>
                  <option value="assignment">Assignment</option>
                  <option value="project">Project</option>
                </select>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Assessment Name *
                </label>
                <input
                  type="text"
                  required
                  value={formData.assessment_name}
                  onChange={(e) => setFormData({ ...formData, assessment_name: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500"
                  placeholder="e.g., Midterm Exam"
                />
              </div>
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Max Marks *
                  </label>
                  <input
                    type="number"
                    required
                    min="0"
                    step="0.01"
                    value={formData.max_marks}
                    onChange={(e) => setFormData({ ...formData, max_marks: e.target.value })}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Obtained Marks *
                  </label>
                  <input
                    type="number"
                    required
                    min="0"
                    step="0.01"
                    value={formData.obtained_marks}
                    onChange={(e) => setFormData({ ...formData, obtained_marks: e.target.value })}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500"
                  />
                </div>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Grade *
                </label>
                <select
                  required
                  value={formData.grade}
                  onChange={(e) => setFormData({ ...formData, grade: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500"
                >
                  <option value="A+">A+</option>
                  <option value="A">A</option>
                  <option value="B+">B+</option>
                  <option value="B">B</option>
                  <option value="C+">C+</option>
                  <option value="C">C</option>
                  <option value="D">D</option>
                  <option value="F">F</option>
                </select>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Assessment Date *
                </label>
                <input
                  type="date"
                  required
                  value={formData.assessment_date}
                  onChange={(e) => setFormData({ ...formData, assessment_date: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Remarks
                </label>
                <textarea
                  value={formData.remarks}
                  onChange={(e) => setFormData({ ...formData, remarks: e.target.value })}
                  rows="2"
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500"
                />
              </div>
              <div className="flex justify-end space-x-3">
                <button
                  type="button"
                  onClick={() => {
                    setShowModal(false)
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
                  Submit
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}

export default Grades
