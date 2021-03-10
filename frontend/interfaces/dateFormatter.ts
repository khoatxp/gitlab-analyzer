const formatDate = (date: string) => {
    const options = {year: 'numeric', month: 'long', day: 'numeric', hour: 'numeric', minute: 'numeric'};
    return new Date(date).toLocaleDateString("en-US", options);
};

export default formatDate;