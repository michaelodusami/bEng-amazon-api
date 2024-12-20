<script>
    import {logoutUser} from "$lib/stores/user"
    import { goto } from "$app/navigation";
  
    function handleLogout() {
      logoutUser(); // Clear user state
      window.location.href = '/login'; // Redirect to login page
    }
  </script>
  
  <button on:click={handleLogout} class="text-sm">
    Logout
  </button>
  