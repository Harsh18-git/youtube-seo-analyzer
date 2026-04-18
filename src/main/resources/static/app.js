// Runs when user clicks Analyze button
async function analyzeVideo() {
  const url = document.getElementById('videoUrl').value.trim();
  if (!url) {
    alert('Please enter a YouTube URL!');
    return;
  }

  // Show spinner, hide old results
  document.getElementById('loading').classList.remove('d-none');
  document.getElementById('results').classList.add('d-none');

  try {
    // Send URL to Spring Boot backend
    const response = await fetch('/api/analyze', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ url: url })
    });

    const data = await response.json();
    showResults(data);

  } catch (err) {
    alert('Something went wrong: ' + err.message);
  } finally {
    // Always hide spinner whether success or error
    document.getElementById('loading').classList.add('d-none');
  }
}

// Fills all cards with the data received from backend
function showResults(data) {

  // Thumbnail
  document.getElementById('thumbnail').src = data.thumbnailUrl;
  document.getElementById('downloadBtn').href = data.thumbnailUrl;

  // Title
  document.getElementById('titleDisplay').textContent = data.title;

  // SEO Score circle — green if good, orange if average, red if poor
  const circle = document.getElementById('scoreCircle');
  document.getElementById('scoreNumber').textContent = data.seoScore;
  circle.style.background =
    data.seoScore >= 70 ? '#198754' :
    data.seoScore >= 40 ? '#fd7e14' : '#dc3545';

  // Feedback badges
  setBadge('titleFeedback', data.titleFeedback);
  setBadge('descFeedback', data.descriptionFeedback);

  // Tags
  const tagBox = document.getElementById('tagsContainer');
  tagBox.innerHTML = '';
  (data.tags || []).forEach(tag => {
    const span = document.createElement('span');
    span.className = 'tag-pill';
    span.textContent = tag;
    tagBox.appendChild(span);
  });

  // Suggestions
  const ul = document.getElementById('suggestionsList');
  ul.innerHTML = '';
  (data.suggestions || []).forEach(s => {
    const li = document.createElement('li');
    li.className = 'list-group-item';
    li.textContent = s;
    ul.appendChild(li);
  });

  // Show the results section
  document.getElementById('results').classList.remove('d-none');
}

// Sets badge color based on feedback text
function setBadge(id, text) {
  const el = document.getElementById(id);
  el.textContent = text;
  el.className = 'badge ' + (
    text === 'Good Length'  ? 'bg-success' :
    text === 'Too Short' || text === 'Missing' ? 'bg-danger' :
    'bg-warning text-dark'
  );
}

// Copies all tags to clipboard
function copyTags() {
  const tags = [...document.querySelectorAll('.tag-pill')].map(t => t.textContent);
  navigator.clipboard.writeText(tags.join(', '));
  alert('Tags copied to clipboard!');
}

// Light / Dark mode toggle
function toggleTheme() {
  const html = document.documentElement;
  const newTheme = html.getAttribute('data-theme') === 'light' ? 'dark' : 'light';
  html.setAttribute('data-theme', newTheme);
  document.getElementById('themeBtn').textContent =
    newTheme === 'dark' ? '☀️ Light Mode' : '🌙 Dark Mode';
  localStorage.setItem('theme', newTheme);
}

// On page load — restore saved theme + Enter key support
window.onload = () => {
  const saved = localStorage.getItem('theme');
  if (saved) {
    document.documentElement.setAttribute('data-theme', saved);
    document.getElementById('themeBtn').textContent =
      saved === 'dark' ? '☀️ Light Mode' : '🌙 Dark Mode';
  }

  document.getElementById('videoUrl').addEventListener('keypress', e => {
    if (e.key === 'Enter') analyzeVideo();
  });
};