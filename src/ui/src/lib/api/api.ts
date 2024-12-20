
export type LoginRequest = {
    email: string
    password: string
}

export type RegisterRequest = {
    name: string
    email: string
    password: string
}

export type User = {
    id: number
    name: string
    email: string
}