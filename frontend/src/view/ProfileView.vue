<template>
  <Header />
  <div class="min-h-screen bg-green-50 text-gray-800 font-sans">

    <main class="flex flex-col lg:flex-row p-8 gap-8 justify-center items-start">
      <ProfileCard />
      <ActiveOrdersCard />
    </main>
  </div>
</template>

<script>
import Header from "@/components/Header.vue"
import ProfileCard from "@/components/profile/ProfileCard.vue";
import ActiveOrdersCard from "@/components/profile/ActiveOrdersCard.vue";

import {defineComponent} from "vue";

export default defineComponent({
  components: {ProfileCard, ActiveOrdersCard, Header}
})
</script>
