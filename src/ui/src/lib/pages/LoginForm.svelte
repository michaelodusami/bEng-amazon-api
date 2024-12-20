<script>
    import {goto} from "$app/navigation"
    import axios from "axios"
    const BACKEND_URL = import.meta.env.VITE_BACKEND_URL;
	import { apiRoutes } from "$lib/common/api";
	import { loginUser } from "$lib/stores/user";

    let email = "";
    let password = "";
    let error = "";

    const handleSumbit = async () => {
        try
        {
            const response = await axios.post(BACKEND_URL + apiRoutes.auth.login, JSON.stringify({email, password}));
            if (!response.data){
                error = "Invalid Credentials";
                return;
            }
            await goto("/");
        }
        catch(err)
        {
            error = `An error occurred. Please try again. [Error: ${err}]`;
        }
    }
</script>

<main class="flex items-center justify-center min-h-screen bg-gray-100">
    <div class="w-full max-w-lg p-6 bg-white rounded-lg shadow-md">
        <h1 class="text-4xl font-semibold text-center text-black mb-3">Welcome!</h1>
        <h1 class="text-2xl font-semibold text-center text-gray-700">Fakezon</h1>
        <p class="text-center text-gray-500 mt-2">Welcome back! We are excited for your arrival!</p>
        <div class="flex justify-center mt-4">
            <img src="/logo.png" alt="Logo" class="w-24 h-24">
        </div>

        {#if error}
            <div class="mt-4 text-red-600 bg-red-100 border border-red-400 rounded px-4 py-2">
                {error}
            </div>
        {/if}

        <form class="mt-6" on:submit|preventDefault={handleSumbit}>
            <div class="mb-4">
                <label class="block text-sm font-medium text-gray-600">Email</label>
                <input type="email"
                    bind:value={email}
                    class="w-full px-4 py-3 mt-2 border rounded-lg focus:outline-none focus:ring focus:ring-blue-100 focus:border-blue-200 border-gray-100 text-black"
                    placeholder="Enter your email"
                    required />
            </div>
            <div class="mb-4">
                <label class="block text-sm font-medium text-gray-600">Password</label>
                <input
                    type="password"
                    bind:value={password}
                    class="w-full px-4 py-3 mt-2 border rounded-lg focus:outline-none focus:ring focus:ring-blue-100 focus:border-blue-200 border-gray-100 text-black"
                    placeholder="Enter your password"
                    required
                />
            </div>
            <button
                type="submit"
                class="w-full px-4 py-2 mt-4 font-semibold text-black rounded-lg hover:bg-blue-50 focus:outline-none focus:ring focus:ring-blue-300"
            >
                Login
            </button>
        </form>
        <p class="mt-6 text-sm text-center text-gray-600">
            Don't have an account? <a href="/register" class="text-blue-500 hover:underline">Register</a>
        </p>
    </div>
</main>