import './PublicationPage.css'

import { useParams, Link } from 'react-router-dom';
import { getPublication } from '../services/client';
import { useEffect, useState } from 'react';
import { useEditor, EditorContent } from '@tiptap/react'
import { StarterKit } from '@syfxlin/tiptap-starter-kit'
import { Markdown } from 'tiptap-markdown';

export default function PublicationPage() {
    const { hash } = useParams();
    const [publication, setPublication] = useState();
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(false);

    const editor = useEditor({
        extensions: [StarterKit, Markdown],
        editable: false
    });

    useEffect(() => {
        async function fetchPublication() {
            try {
                const response = await getPublication(hash);
                setPublication(response.data);
            } catch (e) {
                setError(true);
            } finally {
                setLoading(false);
            }
        }
        fetchPublication();
    }, [hash]);

    useEffect(() => {
        if (editor && publication) {
            editor.commands.setContent(publication.content);
        }
    }, [editor, publication]);

    if (loading) return <div>Loading...</div>;

    if (error) {
        return (
            <div className="controls">
                <h1>Publication not found!</h1>
                <p>The publication you are looking for does not exist.</p>
                <button className='btn btn-primary'><Link to="/">Back</Link></button>
            </div>
        )
    }

    return (
        <>
            <div className="editor-container">
                <p className='input-field'>{publication.title}</p>
                <p className='input-field author'>{publication.author || 'Anonymous'}</p>

                <EditorContent editor={editor} />
            </div>
        </>
    );
}