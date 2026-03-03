document.addEventListener("DOMContentLoaded", () => {

  // Auto-dismiss error alert after 5 seconds
  const alertEl = document.querySelector(".alert-error");
  if (alertEl) {
    setTimeout(() => {
      alertEl.style.transition = "opacity 0.6s ease";
      alertEl.style.opacity = "0";
      setTimeout(() => alertEl.remove(), 600);
    }, 5000);
  }

  // Animate progress bars on weather page
  document.querySelectorAll(".progress-bar-fill").forEach(bar => {
    const target = Math.min(parseFloat(bar.dataset.width) || 0, 100);
    bar.style.width = "0%";
    requestAnimationFrame(() => {
      setTimeout(() => { bar.style.width = target + "%"; }, 300);
    });
  });

  // Press "/" to focus the search box (like GitHub)
  document.addEventListener("keydown", e => {
    if (e.key === "/" && document.activeElement.tagName !== "INPUT") {
      e.preventDefault();
      document.getElementById("city-search")?.focus();
    }
  });

  // Live clock in navbar
  const clockEl = document.getElementById("live-time");
  if (clockEl) {
    const tick = () => {
      clockEl.textContent = new Date().toLocaleTimeString("en-US", {
        hour: "2-digit", minute: "2-digit", second: "2-digit", hour12: false
      });
    };
    tick();
    setInterval(tick, 1000);
  }

  // Confirm before deleting a location
  document.querySelectorAll("form[action*='delete']").forEach(form => {
    form.addEventListener("submit", e => {
      if (!confirm("Remove this location from your list?")) e.preventDefault();
    });
  });

  // Hover glow effect on forecast cards
  document.querySelectorAll(".forecast-card").forEach(card => {
    card.addEventListener("mouseenter", () => {
      card.style.boxShadow = "0 8px 32px rgba(59,130,246,0.15)";
    });
    card.addEventListener("mouseleave", () => {
      card.style.boxShadow = "";
    });
  });
});
