import { useEffect, useState } from 'react'
import {
  BrowserRouter,
  Link,
  Navigate,
  Outlet,
  Route,
  Routes,
  useNavigate,
  useSearchParams,
} from 'react-router-dom'

function ProtectedRoute() {
  const token = localStorage.getItem('token')

  if (!token) {
    return <Navigate to="/login" replace />
  }

  return <Outlet />
}

function Layout() {
  const navigate = useNavigate()

  const handleLogout = () => {
    localStorage.removeItem('token')
    navigate('/login')
  }

  return (
    <div>
      <nav>
        <Link to="/expenses">Expenses</Link> |{' '}
        <Link to="/add-expense">Add Expense</Link> |{' '}
        <button type="button" onClick={handleLogout}>
          Logout
        </button>
      </nav>

      <hr />

      <Outlet />
    </div>
  )
}

function Home() {
  return (
    <div>
      <h1>PocketPipo</h1>
      <Link to="/login">Login</Link>
      <br />
      <Link to="/create-user">Create User</Link>
    </div>
  )
}

function Login() {
  const navigate = useNavigate()
  const [searchParams] = useSearchParams()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')

  const created = searchParams.get('created') === 'true'

  const handleSubmit = async (event) => {
    event.preventDefault()
    setError('')

    try {
      const response = await fetch('http://localhost:8080/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email, password }),
      })

      if (!response.ok) {
        throw new Error('Invalid email or password')
      }

      const data = await response.json()
      localStorage.setItem('token', data.token)
      navigate('/expenses')
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <div>
      <h1>Login</h1>

      {created && <p>User created successfully</p>}

      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="email">Email</label>
          <input
            id="email"
            type="email"
            name="email"
            value={email}
            onChange={(event) => setEmail(event.target.value)}
          />
        </div>

        <div>
          <label htmlFor="password">Password</label>
          <input
            id="password"
            type="password"
            name="password"
            value={password}
            onChange={(event) => setPassword(event.target.value)}
          />
        </div>

        {error && <p>{error}</p>}

        <button type="submit">Login</button>
      </form>
    </div>
  )
}

function CreateUser() {
  const navigate = useNavigate()
  const [email, setEmail] = useState('')
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')

  const handleSubmit = async (event) => {
    event.preventDefault()
    setError('')

    try {
      const response = await fetch('http://localhost:8080/user/users', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email, username, password }),
      })

      if (!response.ok) {
        throw new Error('User creation failed')
      }

      navigate('/login?created=true')
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <div>
      <h1>Create User</h1>

      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="create-user-email">Email</label>
          <input
            id="create-user-email"
            type="email"
            name="email"
            value={email}
            onChange={(event) => setEmail(event.target.value)}
          />
        </div>

        <div>
          <label htmlFor="create-user-username">Username</label>
          <input
            id="create-user-username"
            type="text"
            name="username"
            value={username}
            onChange={(event) => setUsername(event.target.value)}
          />
        </div>

        <div>
          <label htmlFor="create-user-password">Password</label>
          <input
            id="create-user-password"
            type="password"
            name="password"
            value={password}
            onChange={(event) => setPassword(event.target.value)}
          />
        </div>

        {error && <p>{error}</p>}

        <button type="submit">Create User</button>
      </form>
    </div>
  )
}

function Expenses() {
  const [expenses, setExpenses] = useState([])
  const [error, setError] = useState('')

  const year = new Date().getFullYear()
  const month = new Date().getMonth() + 1

  useEffect(() => {
    const fetchExpenses = async () => {
      try {
        const token = localStorage.getItem('token')

        const response = await fetch(
          `http://localhost:8080/expense/expenses?year=${year}&month=${month}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        )

        if (!response.ok) {
          throw new Error('Failed to fetch expenses')
        }

        const data = await response.json()
        setExpenses(data)
      } catch (err) {
        setError(err.message)
      }
    }

    fetchExpenses()
  }, [month, year])

  const groupedExpenses = expenses.reduce((accumulator, expense) => {
    const dateKey = expense.date

    if (!accumulator[dateKey]) {
      accumulator[dateKey] = []
    }

    accumulator[dateKey].push(expense)
    return accumulator
  }, {})

  const sortedDates = Object.keys(groupedExpenses).sort((a, b) => b.localeCompare(a))

  return (
    <div>
      <h1>Monthly Expenses</h1>

      {error && <p>{error}</p>}

      {sortedDates.length === 0 && !error && <p>No expenses found for this month.</p>}

      {sortedDates.map((date) => (
        <div key={date}>
          <h2>{date}</h2>
          <ul>
            {groupedExpenses[date].map((expense) => (
              <li key={expense.id}>
                {expense.description} — {expense.amount}
              </li>
            ))}
          </ul>
        </div>
      ))}
    </div>
  )
}

function AddExpense() {
  const navigate = useNavigate()
  const [description, setDescription] = useState('')
  const [amount, setAmount] = useState('')
  const [date, setDate] = useState('')
  const [error, setError] = useState('')

  const handleSubmit = async (event) => {
    event.preventDefault()
    setError('')

    try {
      const token = localStorage.getItem('token')
      const idempotencyKey = crypto.randomUUID()

      const response = await fetch('http://localhost:8080/expense/expenses', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
          'Idempotency-Key': idempotencyKey,
        },
        body: JSON.stringify({
          description,
          amount,
          date,
        }),
      })

      if (!response.ok) {
        throw new Error('Expense creation failed')
      }

      navigate('/expenses')
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <div>
      <h1>Add Expense</h1>

      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="expense-description">Description</label>
          <input
            id="expense-description"
            type="text"
            name="description"
            value={description}
            onChange={(event) => setDescription(event.target.value)}
          />
        </div>

        <div>
          <label htmlFor="expense-amount">Amount</label>
          <input
            id="expense-amount"
            type="number"
            name="amount"
            step="0.01"
            value={amount}
            onChange={(event) => setAmount(event.target.value)}
          />
        </div>

        <div>
          <label htmlFor="expense-date">Date</label>
          <input
            id="expense-date"
            type="date"
            name="date"
            value={date}
            onChange={(event) => setDate(event.target.value)}
          />
        </div>

        {error && <p>{error}</p>}

        <button type="submit">Create Expense</button>
      </form>
    </div>
  )
}

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/create-user" element={<CreateUser />} />

        <Route element={<ProtectedRoute />}>
          <Route element={<Layout />}>
            <Route path="/expenses" element={<Expenses />} />
            <Route path="/add-expense" element={<AddExpense />} />
          </Route>
        </Route>

        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App