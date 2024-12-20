import forms from '@tailwindcss/forms';
import typography from '@tailwindcss/typography';
import type { Config } from 'tailwindcss';

export default {
	content: ['./src/**/*.{html,js,svelte,ts}'],

	theme: {
		extend: {
			colors: {
				fakeazonGreen: '#007600',
				fakeazonPink: '#FF9900',
			  },
		}
	},

	plugins: [typography, forms]
} satisfies Config;
