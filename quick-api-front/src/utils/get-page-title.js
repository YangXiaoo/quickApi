import defaultSettings from '@/settings'

const title = defaultSettings.title || 'Quick Api'

export default function getPageTitle(pageTitle) {
  if (pageTitle) {
    return `${pageTitle} - ${title}`
  }
  return `${title}`
}
