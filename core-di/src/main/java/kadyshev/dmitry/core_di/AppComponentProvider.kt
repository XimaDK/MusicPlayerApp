package kadyshev.dmitry.core_di

import androidx.fragment.app.Fragment

interface AppComponentProvider {
    fun <T> inject(fragment: T) where T : Fragment
}