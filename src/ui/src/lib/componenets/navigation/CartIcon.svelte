<script>
    export let cartCount = 0;
  </script>
  
  <div class="relative flex items-center space-x-1">
    <img src="/nav/cart-icon.svg" alt="Cart" class="h-6" />
    <p>Cart</p>
    {#if cartCount > 0}
      <div class="absolute top-0 right-0 bg-red-500 text-white text-xs rounded-full w-5 h-5 flex items-center justify-center">
        {cartCount}
      </div>
    {/if}
  </div>
  